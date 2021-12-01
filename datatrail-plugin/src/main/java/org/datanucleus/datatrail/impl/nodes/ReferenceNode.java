package org.datanucleus.datatrail.impl.nodes;

import org.datanucleus.datatrail.Node;

public interface ReferenceNode extends Node {
    Object getValue();

    String getVersion();

    String getDescription();
}
