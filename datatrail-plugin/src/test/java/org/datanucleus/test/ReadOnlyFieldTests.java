package org.datanucleus.test;

import com.spotify.hamcrest.pojo.IsPojo;
import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.api.jdo.annotations.ReadOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.jdo.JDOReadOnlyException;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

class ReadOnlyFieldTests extends AbstractTest{

    @PersistenceCapable
    @Version(strategy= VersionStrategy.VERSION_NUMBER, column="VERSN")
    public static class ReadOnlyFields{
        String name;

        @ReadOnly
        String readOnlyName;

        public ReadOnlyFields(String name) {
            this.name = name;
            this.readOnlyName = "ReadOnly_" + name;
        }
    }

    @PersistenceCapable
    @Version(strategy= VersionStrategy.VERSION_NUMBER, column="VERSN")
    @ReadOnly
    public static class ReadOnlyClass{
        String name;

        String readOnlyName;

        public ReadOnlyClass(String name) {
            this.name = name;
            this.readOnlyName = "ReadOnly_" + name;
        }
    }

    @DisplayName("Read Only fields should not be present in DataTrail")
    @Test
    void testReadOnlyFields(){
        executeTx( pm -> {
            ReadOnlyFields pc = new ReadOnlyFields("firstName");
            pm.makePersistent(pc);
        });

        IsPojo<Node> readOnly = getEntity(NodeAction.CREATE, ReadOnlyFields.class, ANY)
                .withProperty("fields", hasItem(
                   getField(NodeType.PRIMITIVE, String.class, "name", "firstName", null )
                ));

        assertThat(audit.getModifications(), contains(readOnly));
    }


    @DisplayName("Read Only classes should not be present in DataTrail")
    @Test
    void testReadOnlyClass(){
        // attempting to commit a ReadOnlyClass throws a JDOReadOnlyException
        executeTx( pm -> Assertions.assertThrows( JDOReadOnlyException.class, () ->{
            ReadOnlyClass pc = new ReadOnlyClass("firstName");
            pm.makePersistent(pc);
            pm.flush();
        }));

        assertThat(audit.getModifications(), hasSize(0));
    }
}
