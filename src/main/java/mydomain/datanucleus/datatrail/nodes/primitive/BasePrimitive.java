package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.Node;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

abstract public class BasePrimitive extends Node{
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected BasePrimitive(Object value, AbstractMemberMetaData mmd, Node parent){
        // an entity is the root node in the tree
        super(mmd, parent);
        this.value = value == null ? null : value.toString();
        setClassName(value, false);
    }

    @Override
    public boolean canProcess(Object value, MetaData md) {
        // can process any field value as a primitive by using the value.toString()
        return true;
    }

}