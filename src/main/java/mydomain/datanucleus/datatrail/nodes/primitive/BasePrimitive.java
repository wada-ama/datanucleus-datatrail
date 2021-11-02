package mydomain.datanucleus.datatrail.nodes.primitive;

import mydomain.datanucleus.datatrail.BaseNode;
import org.datanucleus.metadata.AbstractMemberMetaData;

abstract public class BasePrimitive extends BaseNode {
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected BasePrimitive(Object value, AbstractMemberMetaData mmd, BaseNode parent){
        // an entity is the root node in the tree
        super(mmd, parent);
        this.value = value == null ? null : value.toString();
        setClassName(value, false);
    }

}
