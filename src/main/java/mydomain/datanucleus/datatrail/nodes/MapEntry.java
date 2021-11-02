package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import mydomain.datanucleus.datatrail.BaseNode;

/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntry as Jackson will automatically serialize those
 * differently
 */
public class MapEntry extends BaseNode {
    BaseNode key;
    BaseNode value;

    public MapEntry(BaseNode key, BaseNode value) {
        super(null, null);
        this.key = key;
        this.value = value;
    }

    @JsonProperty("key")
    public BaseNode getKey() {
        return key;
    }

    @JsonProperty("value")
    @Override
    public BaseNode getValue() {
        return value;
    }


}
