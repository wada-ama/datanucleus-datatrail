package mydomain.datanucleus.datatrail.nodes.map;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;

@NodeDefinition(type=NodeType.MAP, action = NodeAction.CREATE)
public class Create extends BaseMap {

    protected Create(Map value, AbstractMemberMetaData mmd, Node parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the key/value pairs in the map
     * @param map
     */
    @Override
    protected void addElements( Map map ){
        // all new values, so use the raw collection values
        map.entrySet().stream().forEach(element -> {
            Node key = getFactory().createNode(NodeAction.CREATE, ((Map.Entry)element).getKey(), null, this).get();
            Node value = getFactory().createNode(NodeAction.CREATE, ((Map.Entry)element).getValue(), null, this).get();

            this.added.add(new MapEntry(key, value));
        });
    }
}
