package mydomain.datanucleus.datatrail.nodes.map;

import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.Map;

@NodeDefinition(type=NodeType.MAP, action = NodeAction.DELETE)
public class Delete extends BaseMap {
    protected Delete(Map value, AbstractMemberMetaData mmd, Node parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the elements in the collection
     * @param map
     */
    @Override
    protected void addElements( Map map ){
        // all new values, so use the raw collection values
        map.entrySet().stream().forEach(element -> {
            Node key = getFactory().createNode(NodeAction.CREATE, ((Map.Entry)element).getKey(), null, this).get();
            Node value = getFactory().createNode(NodeAction.CREATE, ((Map.Entry)element).getValue(), null, this).get();

            this.removed.add(new MapEntry(key, value));
        });
    }
}
