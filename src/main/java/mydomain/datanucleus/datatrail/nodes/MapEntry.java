package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import mydomain.datanucleus.datatrail.Node;

import java.util.Arrays;

/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntry as Jackson will automatically serialize those
 * differently
 */
public class MapEntry extends BaseNode implements Updatable{
    Node key;
    Node value;

    public MapEntry(Node key, Node value) {
        super(null, null);
        this.key = key;
        this.value = value;
    }

    @JsonProperty("key")
    public Node getKey() {
        return key;
    }

    @JsonProperty("value")
    @Override
    public Node getValue() {
        return value;
    }

    @Override
    public void updateFields() {
        Arrays.asList(key, value).stream().filter(node -> node instanceof Updatable).forEach( node -> ((Updatable)node).updateFields());
    }
}
