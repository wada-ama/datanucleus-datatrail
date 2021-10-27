package mydomain.datanucleus.datatrail.nodes.update;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTrackable;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.stream.Collectors;

public class Collection extends ContainerNode {
    @Override
    public NodeType getType() {
        return NodeType.COLLECTION;
    }

    @Override
    public Action getAction() {
        return Action.UPDATE;
    }

    public Collection(Object value, AbstractMemberMetaData mmd, Node parent) {
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
            added = (java.util.Collection<Node>) changeTracker.getAdded().stream().map(o -> NodeFactory.getInstance().createNode(o, getAction(), null, this)).collect(Collectors.toList());
            removed = (java.util.Collection<Node>) changeTracker.getRemoved().stream().map(o -> NodeFactory.getInstance().createNode(o, getAction(), null, this)).collect(Collectors.toList());
        } else {
            for (Object element : collection) {
                contents.add(NodeFactory.getInstance().createNode(element, getAction(), null, this));
            }
        }
    }
}
