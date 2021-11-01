package mydomain.datanucleus.datatrail.nodes.collection;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

@NodeDefinition(type = NodeType.COLLECTION, action = Node.Action.CREATE)
public class CollectionFactory implements NodeFactory {
    @Override
    public boolean supports(Object value, MetaData md) {
        // can process any field that is identified as a collection
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData) md).hasCollection();

    }

    @Override
    public Optional<Node> create(Node.Action action, Object value, MetaData md, Node parent) {
        if (!supports(value, md))
            return Optional.empty();

        switch(action){
            case CREATE:
                return Optional.of(new Create( value, (AbstractMemberMetaData) md, parent));
            case DELETE:
                return Optional.of(new Delete( value, (AbstractMemberMetaData) md, parent));
            case UPDATE:
                return Optional.of(new Update( value, (AbstractMemberMetaData) md, parent));
            default:
                return Optional.empty();
        }
    }
}
