package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import mydomain.datanucleus.datatrail.ITrailDesc;
import mydomain.datanucleus.datatrail.Node;
import org.datanucleus.enhancement.Persistable;
import org.datanucleus.identity.DatastoreId;
import org.datanucleus.identity.IdentityUtils;
import org.datanucleus.metadata.MetaData;

import javax.jdo.JDOHelper;
import java.lang.ref.WeakReference;

abstract public class AbstractReferenceNode extends BaseNode implements Updatable, ReferenceNode {

    final protected WeakReference<Persistable> source;
    protected String version;
    protected String description;

    protected AbstractReferenceNode(Persistable source, MetaData mmd, Node parent) {
        super(mmd, parent);
        this.source = new WeakReference<Persistable>(source);
        setId(source);
        setVersion(source);
        setDescription(source);
        // set the class name to come from the source classname and not only from the metadata
        setClassName(source, true);
    }

    /**
     * Helper method to set the Id based on the the type of identity of the persistable object.
     * Supports application-id and datastore identity
     * @param pc
     */
    protected void setId(Persistable pc){
        if( pc == null )
            return;

        Object objectId = pc.dnGetObjectId();

        if( objectId == null ) {
            value = null;
        } else if(IdentityUtils.isDatastoreIdentity( objectId ) ) {
            value = ((DatastoreId) objectId).getKeyAsObject().toString();
        } else {
            value = objectId.toString();
        }
    }

    protected void setVersion(Persistable pc){
        if( JDOHelper.getVersion(pc) == null ){
            this.version = null;
        } else {
            this.version = JDOHelper.getVersion(pc).toString();
        }
    }

    protected void setDescription(Object field){
        if( field instanceof ITrailDesc){
            description = ((ITrailDesc)field).minimalTxtDesc();
        }
    }

    @Override
    public void updateFields() {
        setId(getSource());
        setVersion(getSource());
    }


    @JsonIgnore
    public Persistable getSource() {
        return source.get();
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }


}
