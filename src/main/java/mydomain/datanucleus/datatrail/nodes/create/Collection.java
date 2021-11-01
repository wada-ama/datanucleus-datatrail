package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

public class Collection extends ContainerNode {

    @NodeDefinition(type=NodeType.COLLECTION, action = Node.Action.CREATE)
    static public class MapFactory implements mydomain.datanucleus.datatrail.nodes.NodeFactory {
        @Override
        public boolean supports(Object value, MetaData md) {
            // can process any field that is identified as a collection
            return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasCollection();

        }

        @Override
        public Optional<Node> create(Object value, MetaData md, Node parent) {
            if( !supports( value, md ))
                return Optional.empty();

            return Optional.of(new Collection(value, (AbstractMemberMetaData) md, parent));
        }
    }

    protected Collection(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements((java.util.Collection) value);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    private void addElements( java.util.Collection elements ){
        // all new values, so use the raw collection values
        for(Object element : elements )
            this.added.add(NodeFactory.getInstance().createNode(element, Action.CREATE, null, this));
    }


    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasCollection();
    }

}
