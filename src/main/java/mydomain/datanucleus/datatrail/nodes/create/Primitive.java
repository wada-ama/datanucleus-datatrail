package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

public class Primitive extends Node{

    @NodeDefinition(type=NodeType.PRIMITIVE, action = Node.Action.CREATE)
    @NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
    static public class PrimitiveFactory implements NodeFactory{
        @Override
        public boolean supports(Object value, MetaData md) {
            // can process any value as a primitive by using the value.toString()
            return md instanceof AbstractMemberMetaData;
        }

        @Override
        public Optional<Node> create(Object value, MetaData md, Node parent) {
            if( !supports( value, md ))
                return Optional.empty();

            return Optional.of(new Primitive(value, (AbstractMemberMetaData) md, parent));
        }
    }

    /**
     * Default constructor.  Should only be called via the NodeFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected Primitive(Object value, AbstractMemberMetaData mmd, Node parent){
        // an entity is the root node in the tree
        super(mmd, parent);
        this.value = value == null ? null : value.toString();
        setClassName(value, false);
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        // can process any field value as a primitive by using the value.toString()
        return md instanceof AbstractMemberMetaData;
    }
}
