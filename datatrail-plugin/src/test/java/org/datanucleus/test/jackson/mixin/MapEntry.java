package org.datanucleus.test.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.spi.Node;


/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntryImpl as Jackson will automatically serialize those
 * differently
 */

public interface MapEntry extends Node{
    @JsonProperty("key")
    Node getKey();

    @JsonProperty("value")
    Node getValue();
}
