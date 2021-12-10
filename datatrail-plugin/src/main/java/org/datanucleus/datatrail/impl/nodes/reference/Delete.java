package org.datanucleus.datatrail.impl.nodes.reference;

import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.NodeFactory;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.datatrail.impl.nodes.NodeDefinition;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.REF, action = NodeAction.DELETE)
public class Delete extends BaseReference {
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param mmd
     * @param parent
     */
    protected Delete(final Persistable value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(value, mmd, parent, factory);
    }
}
