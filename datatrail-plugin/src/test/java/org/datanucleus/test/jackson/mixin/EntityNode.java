package org.datanucleus.test.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.spi.Node;
import org.datanucleus.datatrail.spi.NodeAction;

import java.time.Instant;
import java.util.Set;

public interface EntityNode extends ReferenceNode {
    @JsonProperty
    Set<Node> getFields();

    @JsonProperty
    Instant getDateModified();

    @JsonProperty("user")
    String getUsername();

    @JsonProperty("txId")
    String getTransactionId();

    @JsonProperty("action")
    NodeAction getAction();
}
