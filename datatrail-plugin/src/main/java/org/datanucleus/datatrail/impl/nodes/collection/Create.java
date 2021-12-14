package org.datanucleus.datatrail.impl.nodes.collection;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.spi.NodeAction;
import org.datanucleus.datatrail.spi.NodeType;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.COLLECTION, action = NodeAction.CREATE)
public class Create extends BaseCollection {

    protected Create(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
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
            getFactory().createNode(NodeAction.CREATE, element, null, this).ifPresent(node -> added.add(node));
    }


}
