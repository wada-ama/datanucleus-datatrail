package mydomain.datanucleus.datatrail.nodes.reference;

import mydomain.datanucleus.datatrail.AbstractNodeFactory;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Optional;

@NodeDefinition(type = NodeType.PRIMITIVE, action = {Node.Action.CREATE, Node.Action.UPDATE, Node.Action.DELETE})
public class ReferenceFactory extends AbstractNodeFactory {

    public ReferenceFactory(DataTrailFactory dataTrailFactory) {
        super(dataTrailFactory);
    }

    @Override
    public boolean supports(Object value, MetaData md) {
        // either the is persistent, or the field is supposed to be persistable (ex: if the value is null)
        return value instanceof Persistable ||
                md instanceof AbstractMemberMetaData && Persistable.class.isAssignableFrom(((AbstractMemberMetaData)md).getType());
    }

    @Override
    public Optional<Node> create(Node.Action action, Object value, MetaData md, Node parent) {
        if( !supports( value, md ))
            return Optional.empty();

        Persistable pc = (Persistable)value;

        switch(action){
            case CREATE:
                return Optional.of(new Create( pc, (AbstractMemberMetaData) md, parent));
            case DELETE:
                return Optional.of(new Delete( pc, (AbstractMemberMetaData) md, parent));
            case UPDATE:
                return Optional.of(new Update( pc, (AbstractMemberMetaData) md, parent));
            default:
                return Optional.empty();
        }
    }
}
