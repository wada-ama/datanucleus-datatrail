package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = Node.Action.CREATE)
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class Primitive extends Node {

    /**
     * Default constructor.  Should only be called via the NodeFactory
     * @param value
     * @param mmd
     * @param parent
     */
    public Primitive(Object value, AbstractMemberMetaData mmd, Node parent){
        // an entity is the root node in the tree
        super(mmd, parent);
        this.value = value == null ? null : value.toString();
        setClassName(value, false);
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        // can process any value as a primitive by using the value.toString()
        return true;
    }
}
