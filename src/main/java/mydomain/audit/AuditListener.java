package mydomain.audit;

import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.nodes.Updatable;
import org.datanucleus.TransactionEventListener;
import org.datanucleus.enhancement.Persistable;
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
import java.util.HashMap;
import java.util.Map;

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


    // internal non-null valued map to store entity modifications throughout the transaction
    private Map<Object, BaseNode> modifications = new HashMap<Object, BaseNode>(){
        // Do not store null values
        @Override
        public BaseNode put(Object key, BaseNode value) {
            // do not store null values
            if( value == null ){
                return value;
            }

            return super.put(key, value);
        }
    };

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
        if( modifications.containsKey(pc)){
            BaseNode node = modifications.get(pc);
            if( node instanceof Updatable){
                ((Updatable)node).updateFields();
            }
        } else {
            // postStore called for both new objects and updating objects, so need to determine which is the state of the object
            logger.warn("New Persistable not already processed {}", pc.dnGetObjectId());
            modifications.put(pc, dataTrailFactory.createNode(pc, NodeAction.DELETE));
        }
    }

    public void postDelete(InstanceLifecycleEvent event) {
        // TODO handle any pre-delete Instance Callbacks

        NucleusLogger.GENERAL.info("Audit : postDelete for " + ((Persistable) event.getSource()).dnGetObjectId());
//        modifications.push(new Entity((Persistable)event.getSource()));

    }

    public void postLoad(InstanceLifecycleEvent event) {
        NucleusLogger.GENERAL.info("Audit : load for " +
                ((Persistable) event.getSource()).dnGetObjectId());
    }

    public void preStore(InstanceLifecycleEvent event) {
        Persistable pc = (Persistable) event.getSource();

        // postStore called for both new objects and updating objects, so need to determine which is the state of the object
        NodeAction action = pc.dnGetStateManager().isNew(pc) ? NodeAction.CREATE : NodeAction.UPDATE;
        modifications.put(pc, dataTrailFactory.createNode(pc, action));

    }

    public void postStore(InstanceLifecycleEvent event) {
        Persistable pc = (Persistable) event.getSource();
        PersistenceManager pm = (PersistenceManager) pc.dnGetExecutionContext().getOwner();

        if (!(event.getSource() instanceof Persistable)) {
            NucleusLogger.GENERAL.debug("Nothing to do. No persistable object found. : " + event.getSource().getClass().getName());
            return;
        }

        // check to see if the entity is already in the modifications map
        if( modifications.containsKey(pc)){
            BaseNode node = modifications.get(pc);
            if( node instanceof Updatable){
                ((Updatable)node).updateFields();
            }
        } else {
            // postStore called for both new objects and updating objects, so need to determine which is the state of the object
            logger.warn("New Persistable not already processed {}", pc.dnGetObjectId());
            NodeAction action = pc.dnGetStateManager().isNew(pc) ? NodeAction.CREATE : NodeAction.UPDATE;
            modifications.put(pc, dataTrailFactory.createNode(pc, action));
        }


    }

    public void transactionStarted() {
        NucleusLogger.GENERAL.info("Audit : TXN START");
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

    public Collection<BaseNode> getModifications() {
        return modifications.values();
    }
}
