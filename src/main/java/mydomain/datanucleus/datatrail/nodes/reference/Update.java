package mydomain.datanucleus.datatrail.nodes.reference;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.ReferenceNode;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

@NodeDefinition(type=NodeType.REF, action = Node.Action.UPDATE)
public class Update extends ReferenceNode {

    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     * @param value
     * @param mmd
     * @param parent
     */
    public Update(Persistable value, AbstractMemberMetaData mmd, Node parent){
        super(value, mmd, parent);

        if( mmd != null )
            this.name = mmd.getName();
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
        // can process any Persitable object that is passed as a field
        if( !(md instanceof AbstractMemberMetaData )) {
            return false;
        }

        AbstractMemberMetaData mmd = (AbstractMemberMetaData) md;

        // either the is persistent, or the field is supposed to be persistable
        // TODO remove the value instanceof Persistable - redundant
        return value instanceof Persistable || Persistable.class.isAssignableFrom(mmd.getType());
    }

}
