package org.datanucleus.datatrail.impl.nodes.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.nodes.BaseNode;
import org.datanucleus.datatrail.impl.nodes.Updatable;

import java.util.stream.Stream;

/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntry as Jackson will automatically serialize those
 * differently
 */
public class MapEntry extends BaseNode implements Updatable {
    Node key;

    public MapEntry(final Node key, final Node value) {
        super(null, null, null);
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
        return (Node) value;
    }

    @Override
    public void updateFields() {
        Stream.of(key, value).filter(Updatable.class::isInstance).forEach(node -> ((Updatable)node).updateFields());
    }
}
