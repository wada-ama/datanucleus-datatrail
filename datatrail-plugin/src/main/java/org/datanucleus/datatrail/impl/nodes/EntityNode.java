package org.datanucleus.datatrail.impl.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.NodeAction;

import java.time.Instant;
import java.util.Set;

public interface EntityNode extends ReferenceNode{
    @JsonProperty
    Set<Node> getFields();

    @JsonProperty
    Instant getDateModified();

    @JsonProperty("user")
    String getUsername();

    @JsonProperty("txId")
    String getTransactionId();

    @JsonIgnore(false)
    @JsonProperty("action")
    NodeAction getAction();
}
