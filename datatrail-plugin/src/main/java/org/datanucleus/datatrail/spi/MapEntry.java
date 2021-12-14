package org.datanucleus.datatrail.spi;


/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntryImpl as Jackson will automatically serialize those
 * differently
 */

public interface MapEntry extends Node{
    Node getKey();

    Node getValue();
}
