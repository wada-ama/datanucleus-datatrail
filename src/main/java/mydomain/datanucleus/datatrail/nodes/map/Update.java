package mydomain.datanucleus.datatrail.nodes.map;

import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTrackable;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@NodeDefinition(type=NodeType.MAP, action = NodeAction.UPDATE)
public class Update extends BaseMap {

    protected Update(Map value, AbstractMemberMetaData mmd, BaseNode parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the elements in the collection
     * @param map
     */
    @Override
    protected void addElements( java.util.Map map ){
        // check if the map is trackable
        if( map instanceof ChangeTrackable && ((ChangeTrackable)map).getChangeTracker().isTracking()){
            // get the tracker
            ChangeTracker changeTracker = ((ChangeTrackable)map).getChangeTracker();
            this.changed = (java.util.Collection<BaseNode>) changeTracker.getChanged().stream().map(o -> {
                Entry keyValue = (Entry)o;
                BaseNode key = getFactory().createNode(keyValue.getKey(), NodeAction.UPDATE, null, this);
                BaseNode value = getFactory().createNode(map.get(keyValue.getKey()), NodeAction.UPDATE, null, this);
                value.setPrev(getFactory().createNode(keyValue.getValue(), NodeAction.UPDATE, null, this));

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());

            this.added = (java.util.Collection<BaseNode>) changeTracker.getAdded().stream().map(o -> {
                Entry keyValue = (Entry)o;
                BaseNode key = getFactory().createNode(keyValue.getKey(), NodeAction.UPDATE, null, this);
                BaseNode value = getFactory().createNode(keyValue.getValue(), NodeAction.UPDATE, null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());

            this.removed = (java.util.Collection<BaseNode>) changeTracker.getRemoved().stream().map(o -> {
                Entry keyValue = (Entry)o;
                BaseNode key = getFactory().createNode(keyValue.getKey(), NodeAction.UPDATE, null, this);
                BaseNode value = getFactory().createNode(keyValue.getValue(), NodeAction.UPDATE, null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());
        } else {

            // not a trackable map
            this.contents = (java.util.Collection<BaseNode>) map.entrySet().stream().map(o -> {
                Entry keyValue = (Entry)o;
                BaseNode key = getFactory().createNode(keyValue.getKey(), NodeAction.UPDATE, null, this);
                BaseNode value = getFactory().createNode(keyValue.getValue(), NodeAction.UPDATE, null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());
        }

    }
}
