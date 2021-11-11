package mydomain.datanucleus.datatrail.nodes.reference;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.nodes.AbstractReferenceNode;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;

abstract public class BaseReference extends AbstractReferenceNode {
    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected BaseReference(Persistable value, AbstractMemberMetaData mmd, Node parent){
        super(value, mmd, parent);

        if( mmd != null )
            this.name = mmd.getName();
    }

}
