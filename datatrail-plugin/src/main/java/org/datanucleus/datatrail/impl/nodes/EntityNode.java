package org.datanucleus.datatrail.impl.nodes;

import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.NodeAction;

import java.time.Instant;
import java.util.Set;

public interface EntityNode extends ReferenceNode{
    Set<Node> getFields();

    Instant getDateModified();

    String getUsername();

    String getTransactionId();

    NodeAction getAction();
}
