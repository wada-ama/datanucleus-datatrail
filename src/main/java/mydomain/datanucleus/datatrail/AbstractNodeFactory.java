package mydomain.datanucleus.datatrail;

import mydomain.datanucleus.datatrail.nodes.NodeFactory;

abstract public class AbstractNodeFactory implements NodeFactory {

    protected DataTrailFactory dataTrailFactory;

    public AbstractNodeFactory(DataTrailFactory dataTrailFactory) {
        this.dataTrailFactory = dataTrailFactory;
    }

    public DataTrailFactory getDataTrailFactory() {
        return dataTrailFactory;
    }
}
