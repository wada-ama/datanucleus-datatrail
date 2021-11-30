package org.datanucleus.datatrail.impl.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.Node;

public interface ReferenceNode extends Node {
    @JsonProperty("id")
    Object getValue();

    @JsonProperty("version")
    String getVersion();

    @JsonProperty("desc")
    String getDescription();
}
