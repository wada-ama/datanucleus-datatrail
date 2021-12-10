package org.datanucleus.datatrail.impl.nodes.array;

import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.ARRAY, action = NodeAction.CREATE)
public class Create extends BaseArray {

    protected Create(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    @Override
    protected void addElements(final Object[] elements ){
        // all new values, so use the raw collection values
        for(final Object element : elements ) {
            getFactory().createNode(NodeAction.CREATE, element, null, this).ifPresent(node -> contents.add(node));
        }
    }
}
