package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import mydomain.datanucleus.datatrail.Node;

/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntry as Jackson will automatically serialize those
 * differently
 */
public class MapEntry extends BaseNode {
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


}
