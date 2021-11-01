package mydomain.datanucleus.datatrail.nodes.create;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.ReferenceNode;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import mydomain.datanucleus.datatrail.nodes.NodePriority;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

public class Reference extends ReferenceNode {

    @NodeDefinition(type=NodeType.REF, action = Node.Action.CREATE)
    static public class ReferenceFactory implements NodeFactory {
        @Override
        public boolean supports(Object value, MetaData md) {
            // either the is persistent, or the field is supposed to be persistable (ex: if the value is null)
            return value instanceof Persistable ||
                    md instanceof AbstractMemberMetaData && Persistable.class.isAssignableFrom(((AbstractMemberMetaData)md).getType());
        }

        @Override
        public Optional<Node> create(Object value, MetaData md, Node parent) {
            if( !supports( value, md ))
                return Optional.empty();

            return Optional.of(new Reference(value, (AbstractMemberMetaData) md, parent));
        }
    }


    /**
     * Default constructor.  Should only be called via the NodeFactory
     * @param value
     * @param mmd
     * @param parent
     */
    protected Reference(Persistable value, AbstractMemberMetaData mmd, Node parent){
        super(value, mmd, parent);

        if( mmd != null )
            this.name = mmd.getName();
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
