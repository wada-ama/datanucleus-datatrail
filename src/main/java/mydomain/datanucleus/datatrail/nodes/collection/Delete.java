package mydomain.datanucleus.datatrail.nodes.collection;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.COLLECTION, action = NodeAction.DELETE)
public class Delete extends BaseCollection {

    protected Delete(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    @Override
    protected void addElements( java.util.Collection elements ){
        // all new values, so use the raw collection values
        for(Object element : elements )
            this.removed.add(getFactory().createNode(element, NodeAction.DELETE, null, this));
    }

}
