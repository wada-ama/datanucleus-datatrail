package org.datanucleus.datatrail.impl.nodes.map;

import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;
import java.util.Optional;

@NodeDefinition(type=NodeType.MAP, action = NodeAction.DELETE)
public class Delete extends BaseMap {
    protected Delete(final Map value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }

    /**
     * Adds all the elements in the collection
     * @param map
     */
    @Override
    protected void addElements(final Map map ){
        // all new values, so use the raw collection values
        map.entrySet().stream().forEach(element -> {
            final Optional<Node> key = getFactory().createNode(NodeAction.DELETE, ((Map.Entry)element).getKey(), null, this);
            final Optional<Node> value = getFactory().createNode(NodeAction.DELETE, ((Map.Entry)element).getValue(), null, this);

            if( key.isPresent() && value.isPresent() ){
                removed.add(new MapEntryImpl(key.get(), value.get()));
            }
        });
    }
}
