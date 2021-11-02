package mydomain.datanucleus.datatrail.nodes.collection;

import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTrackable;
import mydomain.datanucleus.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.stream.Collectors;

@NodeDefinition(type=NodeType.COLLECTION, action = NodeAction.UPDATE)
public class Update extends BaseCollection {
    protected Update(Object value, AbstractMemberMetaData mmd, BaseNode parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the elements in the collection
     * @param collection
     */
    @Override
    protected void addElements( java.util.Collection collection ){
        if( collection instanceof ChangeTrackable && ((ChangeTrackable)collection).getChangeTracker().isTracking()){
            ChangeTracker changeTracker = ((ChangeTrackable)collection).getChangeTracker();
            added = (java.util.Collection<BaseNode>) changeTracker.getAdded().stream().map(o -> getFactory().createNode(o, NodeAction.UPDATE, null, this)).collect(Collectors.toList());
            removed = (java.util.Collection<BaseNode>) changeTracker.getRemoved().stream().map(o -> getFactory().createNode(o, NodeAction.UPDATE, null, this)).collect(Collectors.toList());
        } else {
            for (Object element : collection) {
                contents.add(getFactory().createNode(element, NodeAction.UPDATE, null, this));
            }
        }
    }

}
