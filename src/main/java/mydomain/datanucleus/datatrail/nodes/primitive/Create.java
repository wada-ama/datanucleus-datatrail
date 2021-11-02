package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = NodeAction.CREATE)
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class Create extends BasePrimitive{

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param mmd
     * @param parent
     */
    protected Create(Object value, AbstractMemberMetaData mmd, BaseNode parent) {
        super(value, mmd, parent);
    }
}
