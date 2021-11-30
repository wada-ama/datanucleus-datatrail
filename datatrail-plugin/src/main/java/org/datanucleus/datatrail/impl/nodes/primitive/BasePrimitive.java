package org.datanucleus.datatrail.impl.nodes.primitive;

import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.nodes.BaseNode;
import org.datanucleus.datatrail.Node;
import org.datanucleus.metadata.AbstractMemberMetaData;

public abstract class BasePrimitive extends BaseNode {
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected BasePrimitive(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory){
        // an entity is the root node in the tree
        super(mmd, parent, factory);
        this.value = ((PrimitiveFactory)getFactory()).getAsString(value);
        setClassName(value, false);
    }
}