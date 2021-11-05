package mydomain.datanucleus.datatrail.nodes.entity;


import mydomain.datanucleus.ExtendedReferentialStateManagerImpl;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.NodeFactory;
import org.datanucleus.api.jdo.NucleusJDOHelper;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import javax.jdo.PersistenceManager;

/**
 * Definition of an Entity that is being Created
 */
@NodeDefinition(type=NodeType.ENTITY, action = NodeAction.DELETE)
public class Delete extends BaseEntity {


    /**
     * Default constructor.  Should only be called via the DataTrailFactory
     *
     * @param value
     * @param md
     * @param parent
     * @param factory
     */
    protected Delete(Persistable value, MetaData md, Node parent, NodeFactory factory) {
        super(value, md, parent, factory);
    }

    @Override
    protected void setFields(Persistable pc){
        if( pc == null )
            return;

        PersistenceManager pm = (PersistenceManager)pc.dnGetExecutionContext().getOwner();
        ExtendedReferentialStateManagerImpl op = (ExtendedReferentialStateManagerImpl)pc.dnGetStateManager();

        // need to include all loaded fields
        for(int position : op.getLoadedFieldNumbers()) {
            Object field = op.provideSavedField(position);
            AbstractMemberMetaData mmd = op.getClassMetaData().getMetaDataForManagedMemberAtAbsolutePosition(position);
            if( mmd.isFieldToBePersisted()){
                fields.add(getFactory().createNode(NodeAction.DELETE, field, mmd, this).get());
            }
        }
    }

}
