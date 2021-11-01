package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

@NodeDefinition(type=NodeType.PRIMITIVE, action = Node.Action.UPDATE)
@NodePriority(priority = NodePriority.LOWEST_PRECEDENCE)
public class Update extends Node {


    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    public Update(Object value, AbstractMemberMetaData mmd, Node parent){
        // an entity is the root node in the tree
        super(mmd, parent);
        this.value = value == null ? null : value.toString();
        setClassName(value, false);
    }

    @Override
    public void setPrev(Object value) {
        // previous must be of same type
        if( value != null && value.getClass() != this.getClass()){
            throw new IllegalArgumentException( "Previous value is not of the same type: " + value.getClass().getName() + " !=" + this.getClass().getName());
        }

        this.prev = this.getClass().cast(value).getValue();
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        // can process any value as a primitive by using the value.toString()
        return true;
    }
}
