package mydomain.datanucleus.datatrail;

import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import mydomain.datanucleus.datatrail.nodes.NodeFactory;
import org.datanucleus.metadata.MetaData;

import java.util.Arrays;

abstract public class AbstractNodeFactory implements NodeFactory {

    protected DataTrailFactory dataTrailFactory;

    public AbstractNodeFactory(DataTrailFactory dataTrailFactory) {
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
    public boolean supports(Node.Action action, Object value, MetaData md) {
        NodeDefinition nodeDefn = this.getClass().getAnnotation(NodeDefinition.class);
        return nodeDefn == null ? false : Arrays.asList(nodeDefn.action()).contains(action);
    }
}
