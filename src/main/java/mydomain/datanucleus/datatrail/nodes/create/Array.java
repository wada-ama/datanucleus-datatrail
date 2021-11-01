package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeFactory;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.slf4j.Logger;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class Array extends ContainerNode {

    @NodeDefinition(type=NodeType.ARRAY, action = Node.Action.CREATE)
    static public class MapFactory implements mydomain.datanucleus.datatrail.nodes.NodeFactory {
        @Override
        public boolean supports(Object value, MetaData md) {
            // can process any field that is identified as an array
            return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasArray();

        }

        @Override
        public Optional<Node> create(Object value, MetaData md, Node parent) {
            if( !supports( value, md ))
                return Optional.empty();

            return Optional.of(new Array(value, (AbstractMemberMetaData) md, parent));
        }
    }


    // get a static slf4j logger for the class
    protected static final Logger logger = getLogger(Array.class);

    protected Array(Object value, AbstractMemberMetaData mmd, Node parent) {
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
            this.contents.add(NodeFactory.getInstance().createNode(element, Action.CREATE, null, this));
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasArray();
    }
}
