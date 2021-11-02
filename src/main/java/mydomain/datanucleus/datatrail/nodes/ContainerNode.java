package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import mydomain.datanucleus.datatrail.Node;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.ArrayList;
import java.util.Collection;

abstract public class ContainerNode extends BaseNode implements Updatable {

    protected Collection<Node> added = new ArrayList<>();
    protected Collection<Node> removed = new ArrayList<>();
    protected Collection<Node> changed = new ArrayList<>();
    protected Collection<Node> contents = new ArrayList<>();

    protected ContainerNode(AbstractMemberMetaData mmd, Node parent) {
        super(mmd, parent);
    }

    @JsonIgnore
    @Override
    public String getValue() {
        return null;
    }

    public Collection<Node> getAdded() {
        return added;
    }

    public Collection<Node> getRemoved() {
        return removed;
    }

    public Collection<Node> getChanged() {
        return changed;
    }

    public Collection<Node> getContents() {
        return contents;
    }

    @JsonIgnore
    @Override
    public String getClassName() {
        return super.getClassName();
    }

    @Override
    public void updateFields() {
        added.stream().filter(node -> node instanceof Updatable).forEach( node -> ((Updatable)node).updateFields());
        removed.stream().filter(node -> node instanceof Updatable).forEach( node -> ((Updatable)node).updateFields());
        changed.stream().filter(node -> node instanceof Updatable).forEach( node -> ((Updatable)node).updateFields());
        contents.stream().filter(node -> node instanceof Updatable).forEach( node -> ((Updatable)node).updateFields());
    }
}
