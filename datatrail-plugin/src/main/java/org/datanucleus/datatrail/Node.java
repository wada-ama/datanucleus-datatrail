package org.datanucleus.datatrail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;

public interface Node {
    /**
     * returns the type of node represented by this object
     * @return
     */
    @JsonProperty("type")
    NodeType getType();

    @JsonProperty("name")
    String getName();

    @JsonProperty("class")
    String getClassName();

    @JsonProperty("value")
    Object getValue();

    @JsonProperty("prev")
    Object getPrev();

    @JsonIgnore
    NodeAction getAction();
}
