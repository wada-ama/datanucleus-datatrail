package org.datanucleus.datatrail.impl.nodes.map;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.spi.NodeAction;
import org.datanucleus.datatrail.spi.NodeType;
import org.datanucleus.datatrail.impl.nodes.BaseNode;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTrackable;
import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;
import java.util.stream.Collectors;

@NodeDefinition(type=NodeType.MAP, action = NodeAction.UPDATE)
public class Update extends BaseMap {

    protected Update(final Map value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }

    /**
     * Adds all the elements in the collection
     * @param map
     */
    @Override
    protected void addElements(final java.util.Map map ){
        // check if the map is trackable
        if( map instanceof ChangeTrackable && ((ChangeTrackable)map).getChangeTracker().isTracking()){
            // get the tracker
            final ChangeTracker changeTracker = ((ChangeTrackable)map).getChangeTracker();
            changed = (java.util.Collection<Node>) changeTracker.getChanged().stream().map(o -> {
                final Map.Entry keyValue = (Map.Entry)o;
                final Node key = getFactory().createNode(NodeAction.UPDATE, keyValue.getKey(), null, this).get();
                final BaseNode value = (BaseNode)getFactory().createNode(NodeAction.UPDATE, map.get(keyValue.getKey()), null, this).get();
                value.setPrev(getFactory().createNode(NodeAction.UPDATE, keyValue.getValue(), null, this).get());

                return new MapEntryImpl(key,value);
            }).collect(Collectors.toSet());

            added = (java.util.Collection<Node>) changeTracker.getAdded().stream().map(o -> {
                final Map.Entry keyValue = (Map.Entry)o;
                final Node key = getFactory().createNode(NodeAction.UPDATE, keyValue.getKey(), null, this).get();
                final Node value = getFactory().createNode(NodeAction.UPDATE, keyValue.getValue(), null, this).get();

                return new MapEntryImpl(key,value);
            }).collect(Collectors.toSet());

            removed = (java.util.Collection<Node>) changeTracker.getRemoved().stream().map(o -> {
                final Map.Entry keyValue = (Map.Entry)o;
                final Node key = getFactory().createNode(NodeAction.UPDATE, keyValue.getKey(), null, this).get();
                final Node value = getFactory().createNode(NodeAction.UPDATE, keyValue.getValue(), null, this).get();

                return new MapEntryImpl(key,value);
            }).collect(Collectors.toSet());
        } else {

            // not a trackable map
            contents = (java.util.Collection<Node>) map.entrySet().stream().map(o -> {
                final Map.Entry keyValue = (Map.Entry)o;
                final Node key = getFactory().createNode(NodeAction.UPDATE, keyValue.getKey(), null, this).get();
                final Node value = getFactory().createNode(NodeAction.UPDATE, keyValue.getValue(), null, this).get();

                return new MapEntryImpl(key,value);
            }).collect(Collectors.toSet());
        }

    }
}
