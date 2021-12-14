package org.datanucleus.datatrail.impl.nodes.collection;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.spi.NodeAction;
import org.datanucleus.datatrail.spi.NodeType;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTrackable;
import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.stream.Collectors;

@NodeDefinition(type=NodeType.COLLECTION, action = NodeAction.UPDATE)
public class Update extends BaseCollection {
    protected Update(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }

    /**
     * Adds all the elements in the collection
     * @param collection
     */
    @Override
    protected void addElements(final java.util.Collection collection ){
        if( collection instanceof ChangeTrackable && ((ChangeTrackable)collection).getChangeTracker().isTracking()){
            final ChangeTracker changeTracker = ((ChangeTrackable)collection).getChangeTracker();
            added = (java.util.Collection<Node>) changeTracker.getAdded().stream().map(o -> getFactory().createNode(NodeAction.UPDATE, o, null, this).get()).collect(Collectors.toList());
            removed = (java.util.Collection<Node>) changeTracker.getRemoved().stream().map(o -> getFactory().createNode(NodeAction.UPDATE, o, null, this).get()).collect(Collectors.toList());
        } else {
            for (final Object element : collection) {
                getFactory().createNode(NodeAction.UPDATE, element, null, this).ifPresent(node -> contents.add(node));
            }
        }
    }

}
