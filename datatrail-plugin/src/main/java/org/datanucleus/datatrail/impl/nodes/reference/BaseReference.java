package org.datanucleus.datatrail.impl.nodes.reference;

import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.nodes.AbstractReferenceNode;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;

public abstract class BaseReference extends AbstractReferenceNode {
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected BaseReference(final Persistable value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory){
        super(value, mmd, parent, factory);

        if( mmd != null )
            name = mmd.getName();
    }

}
