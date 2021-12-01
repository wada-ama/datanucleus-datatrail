package org.datanucleus.datatrail.impl.nodes;

import org.datanucleus.datatrail.Node;


/**
 * Class to represent the key/value information found in the map.  Must not implement a Map.MapEntryImpl as Jackson will automatically serialize those
 * differently
 */

public interface MapEntry extends Node{
    Node getKey();

    Node getValue();
}
