package mydomain.datanucleus.datatrail.nodes.array;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@NodeDefinition(type=NodeType.ARRAY, action = Node.Action.CREATE)
public class Create extends BaseArray {

    protected Create(Object value, AbstractMemberMetaData mmd, Node parent) {
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
            this.contents.add(getFactory().createNode(element, Action.CREATE, null, this));
    }
}
