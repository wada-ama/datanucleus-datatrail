package org.datanucleus.test.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.datatrail.spi.Node;

import java.util.Collection;

public interface ContainerNode extends Node{
    @JsonProperty("added")
    Collection<Node> getAdded();

    @JsonProperty("removed")
    Collection<Node> getRemoved();

    @JsonProperty("changed")
    Collection<Node> getChanged();

    @JsonProperty("contents")
    Collection<Node> getContents();
}
