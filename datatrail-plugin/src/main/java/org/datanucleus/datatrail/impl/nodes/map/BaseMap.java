package org.datanucleus.datatrail.impl.nodes.map;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.impl.nodes.AbstractContainerNode;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;

public abstract class BaseMap extends AbstractContainerNode {


    protected BaseMap(final java.util.Map value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(mmd, parent, factory);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements(value);
    }

    /**
     * Adds all the elements in the map
     * @param map
     */
    protected abstract void addElements(Map map);

}
