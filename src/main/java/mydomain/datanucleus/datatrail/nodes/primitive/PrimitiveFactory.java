package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

@NodeDefinition(type = NodeType.PRIMITIVE, action = {Node.Action.CREATE, Node.Action.UPDATE, Node.Action.DELETE})
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class PrimitiveFactory implements NodeFactory {
    @Override
    public boolean supports(Object value, MetaData md) {
        // can process any value as a primitive by using the value.toString()
        return true;
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
