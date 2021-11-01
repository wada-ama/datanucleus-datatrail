package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;
import java.util.Set;

public class Map extends ContainerNode {

    @NodeDefinition(type=NodeType.MAP, action = Node.Action.CREATE)
    static public class MapFactory implements NodeFactory {
        @Override
        public boolean supports(Object value, MetaData md) {
            // can process any value as a primitive by using the value.toString()
            return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasMap();
        }

        @Override
        public Optional<Node> create(Override value, MetaData md, Node parent) {
            if( !supports( value, md ))
                return Optional.empty();

            return Optional.of(new Map(value, (AbstractMemberMetaData) md, parent));
        }
    }


    protected Map(java.util.Map value, AbstractMemberMetaData mmd, Node parent) {
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

    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasMap();
    }
}
