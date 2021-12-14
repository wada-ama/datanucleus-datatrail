package org.datanucleus.datatrail.spi;

import java.time.Instant;
import java.util.Set;

public interface EntityNode extends ReferenceNode{
    Set<Node> getFields();

    Instant getDateModified();

    String getUsername();

    String getTransactionId();
}
