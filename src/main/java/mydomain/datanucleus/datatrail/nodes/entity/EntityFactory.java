package mydomain.datanucleus.datatrail.nodes.entity;

import mydomain.datanucleus.datatrail.AbstractNodeFactory;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

@NodeDefinition(type = NodeType.ENTITY, action = {NodeAction.CREATE, NodeAction.UPDATE, NodeAction.DELETE})
@NodePriority(priority = NodePriority.HIGHEST_PRECEDENCE)
public class EntityFactory extends AbstractNodeFactory {
    @Override
    public boolean supports(NodeAction action, Object value, MetaData md) {
        // can process any Persitable object that is passed as a class
        return  super.supports(action, value, md)
                && value instanceof Persistable
                && md instanceof AbstractClassMetaData;
    }

    @Override
    public Optional<Node> create(NodeAction action, Object value, MetaData md, Node parent) {
        assertConfigured();
        Optional<Node> node = Optional.empty();
        if( supports(action, value, md )) {
            // create the node internally.
            switch (action) {
                case CREATE:
                    node = Optional.of(new Create((Persistable) value, md, parent, this));
                    break;
                case DELETE:
                    node = Optional.of(new Delete((Persistable) value, md, parent, this));
                    break;
                case UPDATE:
                    node = Optional.of(new Update((Persistable) value, md, parent, this));
                    break;
            }
        }

        // if this factory is unable to create the node, then delegate to the data trail factory
        return node.isPresent() ? node : Optional.of(dataTrailFactory.createNode(value, action, md, parent));
    }
}
