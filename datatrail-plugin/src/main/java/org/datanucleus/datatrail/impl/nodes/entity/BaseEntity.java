package org.datanucleus.datatrail.impl.nodes.entity;


import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.TransactionInfo;
import org.datanucleus.datatrail.impl.nodes.AbstractReferenceNode;
import org.datanucleus.datatrail.impl.nodes.EntityNode;
import org.datanucleus.datatrail.impl.nodes.Updatable;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.MetaData;

import javax.jdo.PersistenceManager;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Definition of an Entity that is being Created
 */
public abstract class BaseEntity extends AbstractReferenceNode implements EntityNode {
    protected Set<Node> fields = new HashSet<>();
    protected TransactionInfo txInfo;

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param md
     * @param parent
     */
    protected BaseEntity(final Persistable value, final MetaData md, final Node parent, final NodeFactory factory){
        // an entity is the root node in the tree
        super(value, md,null, factory);
        this.factory = factory;
        setFields(value);
        updateTxDetails();
    }


    protected abstract void setFields(Persistable pc);

    @Override
    public Set<Node> getFields() {
        return fields;
    }

    @Override
    public Instant getDateModified() {
        return txInfo.getDateModified();
    }

    @Override
    public String getUsername() {
        return txInfo.getUsername();
    }

    @Override
    public String getTransactionId() {
        return txInfo.getTxId();
    }


    @Override
    public void updateFields() {
        super.updateFields();
        updateTxDetails();
        fields.stream().filter(Updatable.class::isInstance).forEach(node -> ((Updatable)node).updateFields());
    }

    /**
     * Ensures that a {@link TransactionInfo} object is assigned to the transaction.  If it is missing, create one
     */
    private void updateTxDetails() {
        final PersistenceManager pm = (PersistenceManager)getSource().dnGetExecutionContext().getOwner();
        txInfo = (TransactionInfo) pm.getUserObject(TransactionInfo.class.getName());

        if( txInfo == null ){
            // create a new TxInfo object
            txInfo = new TransactionInfo( Instant.now() );
            pm.putUserObject(TransactionInfo.class.getName(), txInfo);
        }
    }

}
