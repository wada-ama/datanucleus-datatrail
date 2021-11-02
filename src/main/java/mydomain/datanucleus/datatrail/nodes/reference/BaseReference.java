package mydomain.datanucleus.datatrail.nodes.reference;

import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.nodes.ReferenceNode;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;

abstract public class BaseReference extends ReferenceNode {
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected BaseReference(Persistable value, AbstractMemberMetaData mmd, BaseNode parent){
        super(value, mmd, parent);

        if( mmd != null )
            this.name = mmd.getName();
    }

}
