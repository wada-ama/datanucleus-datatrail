package org.datanucleus.datatrail.impl.nodes.map;

import org.datanucleus.datatrail.impl.AbstractNodeFactory;
import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Map;
import java.util.Optional;

@NodeDefinition(type = NodeType.MAP, action = {NodeAction.CREATE, NodeAction.UPDATE, NodeAction.DELETE})
public class MapFactory extends AbstractNodeFactory {

    @Override
    public boolean supports(final NodeAction action, final Object value, final MetaData md) {
        // can process any value as a primitive by using the value.toString()
        return super.supports(action, value, md) &&
                ( value instanceof Map ||
                    md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData) md).hasMap() );
    }

    @Override
    public Optional<Node> createNode(final NodeAction action, final Object value, final MetaData md, final Node parent) {
        assertConfigured();
        if (!supports(action, value, md))
            return dataTrailFactory.createNode(value, action, md, parent );

        switch(action){
            case CREATE:
                return Optional.of(new Create( (Map) value, (AbstractMemberMetaData) md, parent, this));
            case DELETE:
                return Optional.of(new Delete( (Map) value, (AbstractMemberMetaData) md, parent, this));
            case UPDATE:
                return Optional.of(new Update( (Map) value, (AbstractMemberMetaData) md, parent, this));
            default:
                return Optional.empty();
        }
    }
}
