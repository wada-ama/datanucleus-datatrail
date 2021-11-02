package mydomain.datanucleus.datatrail.nodes.array;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.ARRAY, action = NodeAction.UPDATE)
public class Update extends BaseArray {

    protected Update(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    @Override
    protected void addElements( Object[] elements ){
        // all new values, so use the raw collection values
        for(Object element : elements )
            this.contents.add(getFactory().createNode(element, NodeAction.UPDATE, null, this));
    }
}
