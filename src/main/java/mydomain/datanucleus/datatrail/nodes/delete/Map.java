package mydomain.datanucleus.datatrail.nodes.delete;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Set;

@NodeDefinition(type=NodeType.MAP, action = Node.Action.DELETE)
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
            Node key = NodeFactory.getInstance().createNode(element.getKey(), Action.DELETE, null, this);
            Node value = NodeFactory.getInstance().createNode(element.getValue(), Action.DELETE, null, this);

            this.removed.add(new MapEntry(key, value));
        }
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasMap();
    }
}
