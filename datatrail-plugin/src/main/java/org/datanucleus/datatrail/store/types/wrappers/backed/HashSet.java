package org.datanucleus.datatrail.store.types.wrappers.backed;

import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTrackable;
import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.datatrail.store.types.wrappers.tracker.CollectionChangeTrackerImpl;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.ObjectProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class HashSet<E> extends org.datanucleus.store.types.wrappers.backed.HashSet<E> implements ChangeTrackable {
    private final transient CollectionChangeTrackerImpl changeTracker;

    /**
     * Constructor, using the ObjectProvider of the "owner" and the field name.
     *
     * @param op  The owner ObjectProvider
     * @param mmd Metadata for the member
     */
    public HashSet(ObjectProvider op, AbstractMemberMetaData mmd) {
        super(op, mmd);
        changeTracker = new CollectionChangeTrackerImpl( this, false, true, false);
        changeTracker.startTracking();
    }

    @Override
    public ChangeTracker getChangeTracker() {
        return changeTracker;
    }

    @Override
    public boolean add(E element) {
        final boolean result = super.add(element);
        return add(changeTracker, element, result);
    }

    @Override
    public boolean addAll(Collection c) {
        final boolean result = super.addAll(c);
        return add( changeTracker, c,  result);
    }

    @Override
    public boolean remove(Object element) {
        final boolean result = super.remove(element);
        return remove( changeTracker, element, result);
    }

    @Override
    public boolean remove(Object element, boolean allowCascadeDelete) {
        final boolean result = super.remove(element, allowCascadeDelete);
        return remove( changeTracker, element, result);
    }

    @Override
    public boolean removeAll(Collection elements) {
        // retain a list of elements which are present in the delegate
        Collection existing = (Collection) elements.stream().filter(e -> delegate.contains(e)).collect(Collectors.toCollection(ArrayList::new));

        final boolean result = super.removeAll(elements);
        return remove( changeTracker, existing, result);
    }

    @Override
    public void setValue(java.util.HashSet<E> value) {
        // track the removal of all values
        remove(changeTracker, value, true);
        super.setValue(value);
        // track the addition of all new values
        add(changeTracker, value, true);
    }

}
