package org.datanucleus.test;

import com.spotify.hamcrest.pojo.IsPojo;
import org.datanucleus.datatrail.Node;
import org.datanucleus.datatrail.impl.NodeAction;
import org.datanucleus.datatrail.impl.NodeType;
import org.datanucleus.test.model.BaseTeacher;
import org.datanucleus.test.model.Teacher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

class DatatrailExclusionTest extends AbstractTest{

    @DisplayName("Should exclude object from data trail")
    @Test
    void baseTeacherExcluded(){
        executeTx(pm -> {
            BaseTeacher baseTeacher = new BaseTeacher("baseTeacher");
            pm.makePersistent(baseTeacher);
        });

        assertThat("No entities in audit trail", audit.getModifications(), Matchers.hasSize(0));
    }

    @DisplayName( "Child class with override should be included.  Excluded field should be missing")
    @Test
    void childTeacherIncluded(){
        executeTx(pm -> {
            Teacher teacher = new Teacher("teacher", null);
            pm.makePersistent(teacher);
        });

        IsPojo<Node> teacher = getEntity(NodeAction.CREATE, Teacher.class, "1")
                .withProperty("fields",  containsInAnyOrder(
                        getField(NodeType.PRIMITIVE, String.class, "name", "teacher", null),
                        getField(NodeType.REF, BaseTeacher.class, "mentor", null, null )
                ));
        assertThat(audit.getModifications(), contains(teacher));
    }

    @DisplayName( "Child class with composition should be included in the child class, but excluded as an entity in the DT")
    @Test
    void embededBaseTeacherIncluded(){
        executeTx(pm -> {
            BaseTeacher mentor = new BaseTeacher("mentor");
            Teacher teacher = new Teacher("teacher", mentor);
            pm.makePersistent(teacher);
        });

        IsPojo<Node> teacher = getEntity(NodeAction.CREATE, Teacher.class, ANY )
                .withProperty("fields",  containsInAnyOrder(
                        getField(NodeType.PRIMITIVE, String.class, "name", "teacher", null),
                        getField(NodeType.REF, BaseTeacher.class, "mentor", "1", null)
                ));
        assertThat(audit.getModifications(), contains(teacher));
    }
}
