package org.datanucleus.datatrail.impl.nodes.entity;


import org.datanucleus.datatrail.DataTrailStateManagerImpl;
import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.datatrail.impl.nodes.BaseNode;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

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
    protected Update(final Persistable value, final MetaData md, final Node parent, final NodeFactory factory) {
        super(value, md, parent, factory);
    }

    @Override
    protected void setFields(final Persistable pc) {
        if (pc == null)
            return;

        final DataTrailStateManagerImpl op = (DataTrailStateManagerImpl) pc.dnGetStateManager();

        // need to include all dirty fields
        final int[] absoluteFieldPositions = op.getDirtyFieldNumbers() != null ? op.getDirtyFieldNumbers() : new int[]{};
        for (final int position : absoluteFieldPositions) {
            final Object field = op.provideField(position);
            final Object prevField = op.provideSavedField(position);
            final AbstractMemberMetaData mmd = op.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(position);

            if (mmd.isFieldToBePersisted()) {
                // create the current node and set it's prev value to the prevField value node
                getFactory().createNode(NodeAction.UPDATE, field, mmd, this).ifPresent( current -> {
                    getFactory().createNode(NodeAction.UPDATE, prevField, mmd, this).ifPresent( ((BaseNode)current)::setPrev );
                    fields.add(current);
                });
            }
        }
    }

}
