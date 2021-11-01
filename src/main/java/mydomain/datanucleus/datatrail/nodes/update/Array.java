package mydomain.datanucleus.datatrail.nodes.update;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@NodeDefinition(type=NodeType.ARRAY, action = Node.Action.UPDATE)
public class Array extends ContainerNode {

    // get a static slf4j logger for the class
    protected static final Logger logger = getLogger(Array.class);

    public Array(Object value, AbstractMemberMetaData mmd, Node parent) {
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
    private void addElements( Object[] elements ){
        // all new values, so use the raw collection values
        for(Object element : elements )
            this.contents.add(NodeFactory.getInstance().createNode(element, Action.UPDATE, null, this));
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasArray();
    }
}
