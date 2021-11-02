package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = NodeAction.DELETE)
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class Delete extends BasePrimitive {

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param mmd
     * @param parent
     */
    protected Delete(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(value, mmd, parent);
    }
}
