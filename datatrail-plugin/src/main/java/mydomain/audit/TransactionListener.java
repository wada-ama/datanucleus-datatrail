package mydomain.audit;

import mydomain.datanucleus.datatrail.Node;
import org.datanucleus.TransactionEventListener;
import org.datanucleus.api.jdo.JDOTransaction;

import javax.jdo.PersistenceManager;
import java.util.Collection;
import java.util.Collections;

/**
 * This class represents the listener to attach to a Persistence Manager to track any modifications to entities throughout a transaction.
 * The class will attach itself to the current transaction, and add the required listeners to the persistence manager to advise all entity changes
 */
public class TransactionListener implements TransactionEventListener {

    /**
     * The handler used to process the modified entities at the end of a transaction
     */
    @FunctionalInterface
    public interface DataTrailHandler {
        /**
         * An immutable collection of entity nodes that have been identified as modified during the current transaction
         * @param entities
         */
        void execute(Collection<Node> entities);
    }


    final AuditListener auditListener;
    final DataTrailHandler commitHandler;

    /**
     * Constructor
     * @param commitHandler function to call at the end of the transaction
     */
    public TransactionListener(DataTrailHandler commitHandler) {
        auditListener = new AuditListener();
        this.commitHandler = commitHandler;
    }


    /**
     * Attaches the listener to the given persistence manager.  Only supports the identified classes
     * @param pm
     * @param classes which classes to track. a null (not empty) value will track all classes.
     */
    public void attachListener(PersistenceManager pm, Class... classes){
        pm.addInstanceLifecycleListener(auditListener, classes);
        ((JDOTransaction)pm.currentTransaction()).registerEventListener(this);
    }

    @Override
    public void transactionStarted() {
        // ensures that the audit trail is clear for this transaction
        auditListener.clearModifications();
    }

    @Override
    public void transactionEnded() {

    }

    @Override
    public void transactionPreFlush() {

    }

    @Override
    public void transactionFlushed() {

    }

    @Override
    public void transactionPreCommit() {

    }

    @Override
    public void transactionCommitted() {
        // generate the JSON at the end of the transaction
        commitHandler.execute(Collections.unmodifiableCollection(auditListener.getModifications()));
    }

    @Override
    public void transactionPreRollBack() {

    }

    @Override
    public void transactionRolledBack() {

    }

    @Override
    public void transactionSetSavepoint(String name) {

    }

    @Override
    public void transactionReleaseSavepoint(String name) {

    }

    @Override
    public void transactionRollbackToSavepoint(String name) {

    }
}
