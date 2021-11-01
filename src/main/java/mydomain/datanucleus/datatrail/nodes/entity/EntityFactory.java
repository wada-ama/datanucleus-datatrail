package mydomain.datanucleus.datatrail.nodes.entity;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Map;
import java.util.Optional;

@NodeDefinition(type = NodeType.ENTITY, action = {Node.Action.CREATE, Node.Action.UPDATE, Node.Action.DELETE})
public class EntityFactory implements NodeFactory {
    @Override
    public boolean supports(Object value, MetaData md) {
        // can process any Persitable object that is passed as a class
        return value instanceof Persistable && md instanceof AbstractClassMetaData;
    }

    @Override
    public Optional<Node> create(Node.Action action, Object value, MetaData md, Node parent) {
        if (!supports(value, md))
            return Optional.empty();

        switch(action){
            case CREATE:
                return Optional.of(new Create( (Persistable) value, md, parent));
            case DELETE:
                return Optional.of(new Delete( (Persistable) value, md, parent));
            case UPDATE:
                return Optional.of(new Update( (Persistable) value, md, parent));
            default:
                return Optional.empty();
        }
    }
}
