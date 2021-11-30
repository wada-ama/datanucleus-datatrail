package org.datanucleus.datatrail.impl.nodes.collection;

import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.nodes.AbstractContainerNode;
import org.datanucleus.metadata.AbstractMemberMetaData;

public abstract class BaseCollection extends AbstractContainerNode {

    protected BaseCollection(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(mmd, parent, factory);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements((java.util.Collection) value);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    protected abstract void addElements( java.util.Collection<?> elements );

}
