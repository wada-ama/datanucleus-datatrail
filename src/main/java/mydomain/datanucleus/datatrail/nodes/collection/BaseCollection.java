package mydomain.datanucleus.datatrail.nodes.collection;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.nodes.ContainerNode;
import org.datanucleus.metadata.AbstractMemberMetaData;

abstract public class BaseCollection extends ContainerNode {

    protected BaseCollection(Object value, AbstractMemberMetaData mmd, Node parent) {
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
    abstract protected void addElements( java.util.Collection elements );

}
