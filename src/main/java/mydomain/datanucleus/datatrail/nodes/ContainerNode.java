package mydomain.datanucleus.datatrail.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import mydomain.datanucleus.datatrail.BaseNode;
import org.datanucleus.metadata.AbstractMemberMetaData;

import java.util.ArrayList;
import java.util.Collection;

abstract public class ContainerNode extends BaseNode implements Updatable {

    protected Collection<BaseNode> added = new ArrayList<>();
    protected Collection<BaseNode> removed = new ArrayList<>();
    protected Collection<BaseNode> changed = new ArrayList<>();
    protected Collection<BaseNode> contents = new ArrayList<>();

    protected ContainerNode(AbstractMemberMetaData mmd, BaseNode parent) {
        super(mmd, parent);
    }

    @JsonIgnore
    @Override
    public String getValue() {
        return null;
    }

    public Collection<? extends BaseNode> getAdded() {
        return added;
    }

    public Collection<? extends BaseNode> getRemoved() {
        return removed;
    }

    public Collection<BaseNode> getChanged() {
        return changed;
    }

    public Collection<BaseNode> getContents() {
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
