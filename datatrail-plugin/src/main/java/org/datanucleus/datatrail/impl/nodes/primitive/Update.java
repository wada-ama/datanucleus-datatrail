package org.datanucleus.datatrail.impl.nodes.primitive;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.spi.NodeAction;
import org.datanucleus.datatrail.spi.NodeType;
import org.datanucleus.datatrail.spi.NodeDefinition;
import org.datanucleus.datatrail.spi.Priority;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = NodeAction.UPDATE)
@Priority(priority = Priority.LOWEST_PRECEDENCE)
public class Update extends BasePrimitive {

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param mmd
     * @param parent
     */
    protected Update(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }

    @Override
    public void setPrev(final Object value) {
        // previous must be of same type
        if( value != null && value.getClass() != getClass()){
            throw new IllegalArgumentException( "Previous value is not of the same type: " + value.getClass().getName() + " !=" + getClass().getName());
        }

        prev = value;
    }
}
