package mydomain.datanucleus.datatrail.nodes.map;

import mydomain.datanucleus.datatrail.AbstractNodeFactory;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Map;
import java.util.Optional;

@NodeDefinition(type = NodeType.MAP, action = {NodeAction.CREATE, NodeAction.UPDATE, NodeAction.DELETE})
public class MapFactory extends AbstractNodeFactory {
    public MapFactory(DataTrailFactory dataTrailFactory) {
        super(dataTrailFactory);
    }

    @Override
    public boolean supports(NodeAction action, Object value, MetaData md) {
        // can process any value as a primitive by using the value.toString()
        return super.supports(action, value, md) &&
                ( value instanceof Map ||
                    md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData) md).hasMap() );
    }

    @Override
    public Optional<Node> create(NodeAction action, Object value, MetaData md, Node parent) {
        if (!supports(action, value, md))
            return Optional.empty();

        switch(action){
            case CREATE:
                return Optional.of(new Create( (Map) value, (AbstractMemberMetaData) md, parent));
            case DELETE:
                return Optional.of(new Delete( (Map) value, (AbstractMemberMetaData) md, parent));
            case UPDATE:
                return Optional.of(new Update( (Map) value, (AbstractMemberMetaData) md, parent));
            default:
                return Optional.empty();
        }
    }
}
