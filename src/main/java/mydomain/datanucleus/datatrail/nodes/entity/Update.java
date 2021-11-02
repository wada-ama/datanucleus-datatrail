package mydomain.datanucleus.datatrail.nodes.entity;


import mydomain.datanucleus.ExtendedReferentialStateManagerImpl;
import mydomain.datanucleus.datatrail.nodes.BaseNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.NodeFactory;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import javax.jdo.PersistenceManager;

/**
 * Definition of an Entity that is being Created
 */
@NodeDefinition(type=NodeType.ENTITY, action = NodeAction.UPDATE)
public class Update extends BaseEntity {

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param md
     * @param parent
     * @param factory
     */
    protected Update(Persistable value, MetaData md, Node parent, NodeFactory factory) {
        super(value, md, parent, factory);
    }

    @Override
    protected void setFields(Persistable pc) {
        if (pc == null)
            return;

        PersistenceManager pm = (PersistenceManager) pc.dnGetExecutionContext().getOwner();
        ExtendedReferentialStateManagerImpl op = (ExtendedReferentialStateManagerImpl) pc.dnGetStateManager();

        // need to include all dirty fields
        int[] absoluteFieldPositions = op.getDirtyFieldNumbers() != null ? op.getDirtyFieldNumbers() : new int[]{};
        for (int position : absoluteFieldPositions) {
            Object field = op.provideField(position);
            Object prevField = op.provideSavedField(position);
            AbstractMemberMetaData mmd = op.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(position);

            if (mmd.isFieldToBePersisted()) {
                BaseNode current = (BaseNode)getFactory().createNode(NodeAction.UPDATE, field, mmd, this).get();
                current.setPrev(getFactory().createNode(NodeAction.UPDATE, prevField, mmd, this).get());
                fields.add(current);
            }
        }
    }

}
