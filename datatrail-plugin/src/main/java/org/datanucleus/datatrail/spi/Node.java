package org.datanucleus.datatrail.spi;

public interface Node {
    /**
     * returns the type of node represented by this object
     * @return
     */
    NodeType getType();

    String getName();

    String getClassName();

    Object getValue();

    Object getPrev();

    NodeAction getAction();
}
