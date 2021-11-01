package mydomain.datanucleus.datatrail.nodes.collection;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

@NodeDefinition(type=NodeType.COLLECTION, action = Node.Action.DELETE)
public class Delete extends ContainerNode {

    public Delete(Object value, AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements((java.util.Collection) value);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    private void addElements( java.util.Collection elements ){
        // all new values, so use the raw collection values
        for(Object element : elements )
            this.removed.add(getFactory().createNode(element, Action.DELETE, null, this));
    }


    @Override
    public boolean canProcess(Object value, MetaData md) {
        return md instanceof AbstractMemberMetaData && ((AbstractMemberMetaData)md).hasCollection();
    }

}
