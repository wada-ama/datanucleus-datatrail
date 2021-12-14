package org.datanucleus.datatrail.impl.nodes.array;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeFactory;
import org.datanucleus.datatrail.impl.nodes.AbstractContainerNode;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class BaseArray extends AbstractContainerNode {
    // get a static slf4j logger for the class
    protected static final Logger logger = getLogger(BaseArray.class);

    protected BaseArray(final Object value, final AbstractMemberMetaData mmd, final Node parent, final NodeFactory factory) {
        super(mmd, parent, factory);

        BaseArray.logger.warn("Unable to track changes to objects with arrays. {}.{}", mmd.getClassName(), mmd.getName());

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements((Object[])value);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    protected abstract void addElements( Object[] elements );

}
