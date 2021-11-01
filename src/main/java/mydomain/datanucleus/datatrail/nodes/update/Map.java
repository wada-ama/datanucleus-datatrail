package mydomain.datanucleus.datatrail.nodes.update;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTrackable;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Map.Entry;
import java.util.stream.Collectors;

@NodeDefinition(type=NodeType.MAP, action = Node.Action.UPDATE)
public class Map extends ContainerNode {

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
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), Action.UPDATE, null, this);
                Node value = NodeFactory.getInstance().createNode(map.get(keyValue.getKey()), Action.UPDATE, null, this);
                value.setPrev(NodeFactory.getInstance().createNode(keyValue.getValue(), Action.UPDATE, null, this));

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());

            this.added = (java.util.Collection<Node>) changeTracker.getAdded().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), Action.UPDATE, null, this);
                Node value = NodeFactory.getInstance().createNode(keyValue.getValue(), Action.UPDATE, null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());

            this.removed = (java.util.Collection<Node>) changeTracker.getRemoved().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), Action.UPDATE, null, this);
                Node value = NodeFactory.getInstance().createNode(keyValue.getValue(), Action.UPDATE, null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());
        } else {

            // not a trackable map
            this.contents = (java.util.Collection<Node>) map.entrySet().stream().map(o -> {
                Entry keyValue = (Entry)o;
                Node key = NodeFactory.getInstance().createNode(keyValue.getKey(), Action.UPDATE, null, this);
                Node value = NodeFactory.getInstance().createNode(keyValue.getValue(), Action.UPDATE, null, this);

                return new MapEntry(key,value);
            }).collect(Collectors.toSet());
        }

    }


    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasMap();
    }

}
