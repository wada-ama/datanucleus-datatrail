package mydomain.datanucleus.datatrail.nodes.map;

import mydomain.datanucleus.datatrail.nodes.ContainerNode;
import mydomain.datanucleus.datatrail.BaseNode;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;

abstract public class BaseMap extends ContainerNode {


    protected BaseMap(java.util.Map value, AbstractMemberMetaData mmd, BaseNode parent) {
        super(mmd, parent);

        // value might be null, in which case there is nothing left to do
        if( value == null ){
            return;
        }

        addElements(value);
    }

    /**
     * Adds all the elements in the map
     * @param map
     */
    abstract protected void addElements(Map map);

}
