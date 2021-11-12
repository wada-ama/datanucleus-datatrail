package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ReferenceNode {
    @JsonProperty("id")
    Object getValue();

    @JsonProperty("prev")
    Object getPrev();

    @JsonProperty("version")
    String getVersion();

    @JsonProperty("desc")
    String getDescription();
}
