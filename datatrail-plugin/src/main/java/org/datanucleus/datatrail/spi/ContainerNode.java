package org.datanucleus.datatrail.spi;

import java.util.Collection;

public interface ContainerNode extends Node{
    Collection<Node> getAdded();

    Collection<Node> getRemoved();

    Collection<Node> getChanged();

    Collection<Node> getContents();
}
