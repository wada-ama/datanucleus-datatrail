package mydomain.datanucleus.datatrail2.nodes.update;

import mydomain.datanucleus.datatrail2.ContainerNode;
import mydomain.datanucleus.datatrail2.Node;
import mydomain.datanucleus.datatrail2.NodeFactory;
import mydomain.datanucleus.datatrail2.NodeType;
import mydomain.datanucleus.datatrail2.nodes.MapEntry;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTrackable;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Map extends ContainerNode {

    @Override
    public NodeType getType() {
        return NodeType.MAP;
    }

    @Override
    public Action getAction() {
        return Action.UPDATE;
    }

    public Map(java.util.Map value, AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements(value);
    }

    /**
     * Adds all the elements in the collection
     * @param map
     */
    private void addElements( java.util.Map map ){
        // check if the map is trackable
        if( map instanceof ChangeTrackable && ((ChangeTrackable)map).getChangeTracker().isTracking()){
            // get the tracker
            ChangeTracker changeTracker = ((ChangeTrackable)map).getChangeTracker();
            this.changed = (java.util.Collection<Node>) changeTracker.getChanged().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), getAction(), null, this);
                Node value = NodeFactory.getInstance().createNode(map.get(keyValue.getKey()), getAction(), null, this);
                value.setPrev(NodeFactory.getInstance().createNode(keyValue.getValue(), getAction(), null, this));

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());

            this.added = (java.util.Collection<Node>) changeTracker.getAdded().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), getAction(), null, this);
                Node value = NodeFactory.getInstance().createNode(keyValue.getValue(), getAction(), null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());

            this.removed = (java.util.Collection<Node>) changeTracker.getRemoved().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), getAction(), null, this);
                Node value = NodeFactory.getInstance().createNode(keyValue.getValue(), getAction(), null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());
        } else {

            // not a trackable map
            this.contents = (java.util.Collection<Node>) map.keySet().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), getAction(), null, this);
                Node value = NodeFactory.getInstance().createNode(keyValue.getValue(), getAction(), null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());
        }

    }
}
