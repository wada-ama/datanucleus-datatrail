package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = NodeAction.UPDATE)
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class Update extends BasePrimitive {

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param mmd
     * @param parent
     */
    protected Update(final Object value, final AbstractMemberMetaData mmd, final Node parent) {
        super(value, mmd, parent);
    }

    @Override
    public void setPrev(final Object value) {
        // previous must be of same type
        if( value != null && value.getClass() != getClass()){
            throw new IllegalArgumentException( "Previous value is not of the same type: " + value.getClass().getName() + " !=" + getClass().getName());
        }

        prev = getClass().cast(value).getValue();
    }
}
