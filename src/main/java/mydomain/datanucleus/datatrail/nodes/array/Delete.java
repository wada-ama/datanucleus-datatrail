package mydomain.datanucleus.datatrail.nodes.array;

import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

import static org.slf4j.LoggerFactory.getLogger;

@NodeDefinition(type=NodeType.ARRAY, action = NodeAction.DELETE)
public class Delete extends BaseArray {

    protected Delete(Object value, AbstractMemberMetaData mmd, BaseNode parent) {
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
            this.contents.add(getFactory().createNode(element, NodeAction.DELETE, null, this));
    }
}
