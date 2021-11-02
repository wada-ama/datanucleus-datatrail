package mydomain.datanucleus.datatrail;

import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.MetaData;

import java.util.Arrays;

abstract public class AbstractNodeFactory implements NodeFactory {

    protected DataTrailFactory dataTrailFactory;

    /**
     * Sets the {@link DataTrailFactory} used to create this factory.  In order to protect the structural integrity of the generated Nodes,
     * the dataTrailFactory cannot be changed once it is set.  A new factory needs to be instantiated instead.
     *
     * @param dataTrailFactory
     */
    @Override
    public void setDataTrailFactory(DataTrailFactory dataTrailFactory) {
        if( this.dataTrailFactory != null ){
            throw new IllegalStateException( "Cannot change the DataTrailFactory once it has already been set.  Create a new instance instead");
        }

        this.dataTrailFactory = dataTrailFactory;
    }

    public DataTrailFactory getDataTrailFactory() {
        return dataTrailFactory;
    }

    /**
     * Checks to see if this factory can produce a node for the given parameters
     * @param action
     * @param value the object to be represented by a DataTrail node
     * @param md the metadata relating to the given object
     * @return
     */
    public boolean supports(NodeAction action, Object value, MetaData md) {
        assertConfigured();
        NodeDefinition nodeDefn = this.getClass().getAnnotation(NodeDefinition.class);
        return nodeDefn == null ? false : Arrays.asList(nodeDefn.action()).contains(action);
    }


    /**
     * Ensures that the {@link DataTrailFactory} is set
     */
    protected void assertConfigured() {
        if( dataTrailFactory == null ){
            throw new IllegalStateException( "Factory cannot be used before the DataTrailFactory is set");
        }
    }

}
