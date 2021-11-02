package mydomain.datanucleus.datatrail.nodes.entity;


import mydomain.datanucleus.datatrail.BaseNode;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.NodeFactory;
import org.datanucleus.api.jdo.NucleusJDOHelper;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;
import org.datanucleus.state.ObjectProvider;

import javax.jdo.PersistenceManager;

/**
 * Definition of an Entity that is being Created
 */
@NodeDefinition(type=NodeType.ENTITY, action = NodeAction.CREATE)
public class Create extends BaseEntity {


    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param md
     * @param parent
     * @param factory
     */
    protected Create(Persistable value, MetaData md, BaseNode parent, NodeFactory factory) {
        super(value, md, parent, factory);
    }

    @Override
    protected void setFields(Persistable pc){
        if( pc == null )
            return;

        PersistenceManager pm = (PersistenceManager)pc.dnGetExecutionContext().getOwner();
        ObjectProvider op = (ObjectProvider)pc.dnGetStateManager();

        // need to include all loaded fields
        String[] fieldNames = NucleusJDOHelper.getLoadedFields( pc, pm);
        for(String fieldName : fieldNames) {
            int position = op.getClassMetaData().getAbsolutePositionOfMember(fieldName);
            Object field = op.provideField(position);
            AbstractMemberMetaData mmd = op.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(position);
            if( mmd.isFieldToBePersisted()){
                fields.add(getFactory().createNode( field, NodeAction.CREATE, mmd, this));
            }
        }
    }
}
