package mydomain.datanucleus.datatrail.nodes.collection;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTrackable;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.stream.Collectors;

@NodeDefinition(type=NodeType.COLLECTION, action = Node.Action.UPDATE)
public class Update extends ContainerNode {

    public Update(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements((java.util.Collection) value);
    }

    /**
     * Adds all the elements in the collection
     * @param collection
     */
    private void addElements( java.util.Collection collection ){
        if( collection instanceof ChangeTrackable && ((ChangeTrackable)collection).getChangeTracker().isTracking()){
            ChangeTracker changeTracker = ((ChangeTrackable)collection).getChangeTracker();
            added = (java.util.Collection<Node>) changeTracker.getAdded().stream().map(o -> getFactory().createNode(o, Action.UPDATE, null, this)).collect(Collectors.toList());
            removed = (java.util.Collection<Node>) changeTracker.getRemoved().stream().map(o -> getFactory().createNode(o, Action.UPDATE, null, this)).collect(Collectors.toList());
        } else {
            for (Object element : collection) {
                contents.add(getFactory().createNode(element, Action.UPDATE, null, this));
            }
        }
    }


    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasCollection();
    }

}
