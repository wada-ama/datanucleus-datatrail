package mydomain.datanucleus.datatrail2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.ArrayList;
import java.util.Collection;

abstract public class ContainerNode extends Node {

    protected Collection<Node> added = new ArrayList<>();
    protected Collection<Node> removed = new ArrayList<>();

    protected ContainerNode(AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);
    }

    @JsonIgnore
    @Override
    public String getValue() {
        return null;
    }

    public Collection<? extends Node> getAdded() {
        return added;
    }

    public Collection<? extends Node> getRemoved() {
        return removed;
    }

    @JsonIgnore
    @Override
    public String getClassName() {
        return super.getClassName();
    }

    @Override
    public void updateFields() {
        super.updateFields();
        added.stream().forEach( node -> node.updateFields());
        removed.stream().forEach( node -> node.updateFields());
    }
}
