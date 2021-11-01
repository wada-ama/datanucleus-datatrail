package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = Node.Action.CREATE)
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class Create extends Node{

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected Create(Object value, AbstractMemberMetaData mmd, Node parent){
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
