package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Set;

@NodeDefinition(type=NodeType.MAP, action = Node.Action.CREATE)
public class Map extends ContainerNode {

    public Map(java.util.Map value, AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements(value.entrySet());
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    private void addElements( Set<java.util.Map.Entry> elements ){
        // all new values, so use the raw collection values
        for( java.util.Map.Entry element : elements){
            Node key = NodeFactory.getInstance().createNode(element.getKey(), Action.CREATE, null, this);
            Node value = NodeFactory.getInstance().createNode(element.getValue(), Action.CREATE, null, this);

            this.added.add(new MapEntry(key, value));
        }
    }
}
