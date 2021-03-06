package org.datanucleus.datatrail.impl.nodes.collection;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.spi.NodeAction;
import org.datanucleus.datatrail.spi.NodeType;
import org.datanucleus.datatrail.spi.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.COLLECTION, action = NodeAction.DELETE)
public class Delete extends BaseCollection {

    protected Delete(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    @Override
    protected void addElements(final java.util.Collection elements ){
        // all new values, so use the raw collection values
        for(final Object element : elements )
            getFactory().createNode(NodeAction.DELETE, element, null, this).ifPresent(node -> removed.add(node));
    }

}
