package mydomain.datanucleus.datatrail.nodes.map;

import mydomain.datanucleus.datatrail.ContainerNode;
import mydomain.datanucleus.datatrail.DataTrailFactory;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.datanucleus.datatrail.nodes.MapEntry;
import mydomain.datanucleus.datatrail.nodes.NodeDefinition;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.MetaData;

import java.util.Map;
import java.util.Set;

@NodeDefinition(type=NodeType.MAP, action = Node.Action.CREATE)
public class Create extends BaseMap {

    protected Create(Map value, AbstractMemberMetaData mmd, Node parent) {
        super(value, mmd, parent);
    }

    /**
     * Adds all the elements in the collection
     * @param elements
     */
    @Override
    protected void addElements( Map map ){
        // all new values, so use the raw collection values
        map.entrySet().stream().forEach(element -> {
            Node key = getFactory().createNode(((Map.Entry)element).getKey(), Action.CREATE, null, this);
            Node value = getFactory().createNode(((Map.Entry)element).getValue(), Action.CREATE, null, this);

            this.added.add(new MapEntry(key, value));
        });
    }
}
