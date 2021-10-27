package mydomain.datanucleus.datatrail2.nodes.update;

import mydomain.datanucleus.datatrail2.Node;
import mydomain.datanucleus.datatrail2.NodeType;
import org.datanucleus.metadata.AbstractMemberMetaData;

public class Primitive extends Node {

    @Override
    public NodeType getType() {
        return NodeType.PRIMITIVE;
    }

    @Override
    public Action getAction() {
        return Action.UPDATE;
    }



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
    public void setPrev(Object value) {
        // previous must be of same type
        if( value != null && value.getClass() != this.getClass()){
            throw new IllegalArgumentException( "Previous value is not of the same type: " + value.getClass().getName() + " !=" + this.getClass().getName());
        }

        this.prev = this.getClass().cast(value).getValue();
    }
}
