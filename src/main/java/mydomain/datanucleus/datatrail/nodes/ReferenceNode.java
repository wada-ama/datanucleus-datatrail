package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.datanucleus.enhancement.Persistable;

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
