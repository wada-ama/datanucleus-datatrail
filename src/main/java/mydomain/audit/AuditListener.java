package mydomain.audit;

import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.nodes.Updatable;
import org.datanucleus.TransactionEventListener;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.identity.IdentityReference;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.util.NucleusLogger;
import org.slf4j.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.listener.CreateLifecycleListener;
import javax.jdo.listener.DeleteLifecycleListener;
import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.LoadLifecycleListener;
import javax.jdo.listener.StoreLifecycleListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Class that provides suitable hooks for auditing of a persistence process.
 * <p>
 * This implementation simply logs the audit events.
 */
public class AuditListener implements CreateLifecycleListener,
        DeleteLifecycleListener, LoadLifecycleListener, StoreLifecycleListener, TransactionEventListener {

    // get a static slf4j logger for the class
    protected static final Logger logger = getLogger(AuditListener.class);

    protected DataTrailFactory dataTrailFactory = DataTrailFactory.getDataTrailFactory();


    /**
     * Class to store the Modifications to the objects used in the Audit Trail
     */
    private static class Modifications {
        private Map<Object, Set<Node>> delegate = new ConcurrentHashMap<>();

        private IdentityReference getReference( Object key ){
            return new IdentityReference(key);
        }

        /**
         * Adds the {@link Node} corresponding to the {@link Persistable} object to the list of modifications occured during
         * the current transaction
         * @param pc
         * @param value
         * @return
         */
        public void add(Persistable pc, Node value){
            if( value == null ){
                return;
            }

            IdentityReference key = getReference(pc);

            if(!delegate.containsKey(key)){
                delegate.put(key, new HashSet<>());
            }

            // adds the node to the list, but must follow these rules
            // if CREATE + DELETE ==> Nothing
            // If UPDATE + DELETE ==> DELETE
            // if DELETE ==> DELETE
            // if CREATE + UPDATE + DELETE => Nothing
            // if DELETE + CREATE ==> DELETE (DN won't do the CREATE)
            if( value.getAction() == NodeAction.DELETE ) {
                // remove any CREATE or UPDATE nodes that are part of the same tx as the DELETE superceeds them
                delegate.get(key).removeIf(node -> (node.getAction() == NodeAction.UPDATE));
                boolean createRemoved = delegate.get(key).removeIf(node -> (node.getAction() == NodeAction.CREATE));

                if( createRemoved ){
                    // ignore the DELETE node b/c a CREATED node was found in the same tx, so they cancel each other out
                    return;
                }
            }

            // replace any existing node with the same action with the new value
            delegate.get(key).removeIf( node -> node.getAction() == value.getAction());
            delegate.get(key).add(value);
        }


        /**
         * Gets the node with the specified action
         * @param pc
         * @param action
         * @return Node if present.  null if not
         */
        public Node get(Persistable pc, NodeAction action){
            return delegate.getOrDefault(getReference(pc), Collections.emptySet()).stream()
                    .filter( node -> node.getAction() == action )
                    .findFirst()
                    .orElse(null);
        }


        /**
         * Checks if this {@link Persistable} has already been added to the tracked modifications
         * @param pc
         * @return
         */
        public boolean contains(Persistable pc){
            return delegate.containsKey( getReference(pc));
        }


        /**
         * Gets an unordered Set of all the different modifications that were added to this audit trail.
         * @return
         */
        public Collection<Node> values(){
            return delegate.values().stream()
                    .flatMap(nodes -> nodes.stream())
                    .collect(Collectors.toCollection(HashSet::new));
        }

        /**
         * Clears the list of all modifications
         */
        public void clear(){
            delegate.clear();
        }
    }


    private Modifications modifications = new Modifications();


    public AuditListener() {
    }

    public void postCreate(InstanceLifecycleEvent event) {
        NucleusLogger.GENERAL.info("Audit : create for " +
                ((Persistable) event.getSource()).dnGetObjectId());
    }

    public void preDelete(InstanceLifecycleEvent event) {
        Persistable pc = (Persistable) event.getSource();

        if (!(event.getSource() instanceof Persistable) || JDOHelper.isDetached(event.getPersistentInstance())) {
            NucleusLogger.GENERAL.debug("Nothing to do. No persistable object found. : " + event.getSource().getClass().getName());
            return;
        }

        // force all fields to be loaded before deletion
        ObjectProvider op = (ObjectProvider) ((Persistable) event.getPersistentInstance()).dnGetStateManager();


        // in Optimistic locking mode, the listener is called 2x.
        // event listener called for objects in lifecycle states:
        // - persistent-deleted (just before the object is permanently deleted)
        // - other depending on the state of the object


        // if the object isn't dirty, then need to force the OP to save the initial state of the object
        if (!JDOHelper.isDirty(pc)) {
            op.saveFields();
        }

        // load any extra fields before deletion
        op.loadUnloadedFields();

        // check to see if the entity is already in the modifications map
        if( modifications.contains(pc)){
            Node node = modifications.get(pc, NodeAction.DELETE);
            if( node instanceof Updatable){
                ((Updatable)node).updateFields();
            }
        } else {
            // postStore called for both new objects and updating objects, so need to determine which is the state of the object
            logger.warn("New Persistable not already processed {}", pc.dnGetObjectId());
            modifications.add(pc, dataTrailFactory.createNode(pc, NodeAction.DELETE));
        }
    }

    public void postDelete(InstanceLifecycleEvent event) {
        // TODO handle any pre-delete Instance Callbacks
    }

    public void postLoad(InstanceLifecycleEvent event) {
    }

    public void preStore(InstanceLifecycleEvent event) {
        Persistable pc = (Persistable) event.getSource();

        // postStore called for both new objects and updating objects, so need to determine which is the state of the object
        NodeAction action = pc.dnGetStateManager().isNew(pc) ? NodeAction.CREATE : NodeAction.UPDATE;
        modifications.add(pc, dataTrailFactory.createNode(pc, action));

    }

    public void postStore(InstanceLifecycleEvent event) {
        Persistable pc = (Persistable) event.getSource();
        PersistenceManager pm = (PersistenceManager) pc.dnGetExecutionContext().getOwner();

        if (!(event.getSource() instanceof Persistable)) {
            NucleusLogger.GENERAL.debug("Nothing to do. No persistable object found. : " + event.getSource().getClass().getName());
            return;
        }

        // check to see if the entity is already in the modifications map
        NodeAction action = pc.dnGetStateManager().isNew(pc) ? NodeAction.CREATE : NodeAction.UPDATE;
        if( modifications.contains(pc)){
            Node node = modifications.get(pc, action);
            if( node instanceof Updatable){
                ((Updatable)node).updateFields();
            }
        } else {
            // postStore called for both new objects and updating objects, so need to determine which is the state of the object
            logger.warn("New Persistable not already processed {}", pc.dnGetObjectId());
            modifications.add(pc, dataTrailFactory.createNode(pc, action));
        }


    }

    public void transactionStarted() {
        NucleusLogger.GENERAL.info("Audit : TXN START");
        // ensures that the audit trail is clear for this transaction
        modifications.clear();
    }

    public void transactionEnded() {
    }

    public void transactionPreFlush() {
    }

    public void transactionFlushed() {
    }

    public void transactionPreCommit() {
        NucleusLogger.GENERAL.info("Audit : TXN PRE-COMMIT");
    }

    public void transactionCommitted() {
        NucleusLogger.GENERAL.info("Audit : TXN COMMITTED");
        logger.info(modifications.toString());

        // generate JSON blob

    }

    public void transactionPreRollBack() {
    }

    public void transactionRolledBack() {
    }

    public void transactionSetSavepoint(String name) {
    }

    public void transactionReleaseSavepoint(String name) {
    }

    public void transactionRollbackToSavepoint(String name) {
    }

    public Collection<Node> getModifications() {
        return modifications.values();
    }
}
