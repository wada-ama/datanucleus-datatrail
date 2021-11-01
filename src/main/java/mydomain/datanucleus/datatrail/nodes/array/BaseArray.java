package mydomain.datanucleus.datatrail.nodes.array;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

abstract public class BaseArray extends ContainerNode {
    // get a static slf4j logger for the class
    protected static final Logger logger = getLogger(BaseArray.class);

    protected BaseArray(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);

        logger.warn("Unable to track changes to objects with arrays. {}.{}", mmd.getClassName(), mmd.getName());

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
    abstract protected void addElements( Object[] elements );

    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasArray();
    }
}
