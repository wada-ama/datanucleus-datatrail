package org.datanucleus.datatrail.impl.nodes.map;

import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.impl.nodes.BaseNode;
import org.datanucleus.datatrail.spi.MapEntry;
import org.datanucleus.datatrail.impl.nodes.Updatable;

import java.util.stream.Stream;

/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntryImpl as Jackson will automatically serialize those
 * differently
 */
public class MapEntryImpl extends BaseNode implements Updatable, MapEntry {
    Node key;

    public MapEntryImpl(final Node key, final Node value) {
        super(null, null, null);
        this.key = key;
        this.value = value;
    }

    @Override
    public Node getKey() {
        return key;
    }

    @Override
    public Node getValue() {
        return (Node) value;
    }

    @Override
    public void updateFields() {
        Stream.of(key, value).filter(Updatable.class::isInstance).forEach(node -> ((Updatable)node).updateFields());
    }
}
