package org.datanucleus.datatrail.store.types.wrappers.backed;

import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTrackable;
import org.datanucleus.datatrail.store.types.wrappers.tracker.ChangeTracker;
import org.datanucleus.datatrail.store.types.wrappers.tracker.CollectionChangeTrackerImpl;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.types.scostore.CollectionStore;
import org.datanucleus.store.types.scostore.SetStore;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Collection<E> extends org.datanucleus.store.types.wrappers.backed.Collection<E> implements ChangeTrackable {
    private final transient CollectionChangeTrackerImpl changeTracker;

    public Collection(ObjectProvider op, AbstractMemberMetaData mmd) {
        super(op, mmd);
        changeTracker = new CollectionChangeTrackerImpl( this, true, false, false);
        changeTracker.startTracking();
    }

    public Collection(ObjectProvider ownerOP, AbstractMemberMetaData mmd, boolean allowNulls, CollectionStore backingStore) {
        super(ownerOP, mmd, allowNulls, backingStore);
        changeTracker = new CollectionChangeTrackerImpl( this, true, false, false);
        changeTracker.startTracking();
    }


    @Override
    public boolean add(E element) {
        final boolean result = super.add(element);
        return add(changeTracker, element, result);
    }

    @Override
    public boolean addAll(java.util.Collection c) {
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
    public boolean removeAll(java.util.Collection elements) {
        // retain a list of elements which are present in the delegate
        java.util.Collection existing = (java.util.Collection) elements.stream().filter(e -> delegate.contains(e)).collect(Collectors.toCollection(ArrayList::new));

        final boolean result = super.removeAll(elements);
        return remove( changeTracker, existing, result);
    }

    @Override
    public void setValue(java.util.Collection<E> value) {
        // track the removal of all values
        remove(changeTracker, value, true);
        super.setValue(value);
        // track the addition of all new values
        add(changeTracker, value, true);
    }

    @Override
    public ChangeTracker getChangeTracker() {
        return changeTracker;
    }
}
