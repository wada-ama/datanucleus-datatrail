package org.datanucleus.test;

import com.spotify.hamcrest.pojo.IsPojo;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.model.BaseTeacher;
import mydomain.model.Teacher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class DatatrailExclusionTest extends AbstractTest{

    @Test
    public void baseTeacherExcluded(){
        executeTx(pm -> {
            BaseTeacher baseTeacher = new BaseTeacher("baseTeacher");
            pm.makePersistent(baseTeacher);
        });

        assertThat("No entities in audit trail", audit.getModifications(), Matchers.hasSize(0));
    }

    @Test
    public void childTeacherIncluded(){
        executeTx(pm -> {
            Teacher teacher = new Teacher("teacher", null);
            pm.makePersistent(teacher);
        });

        IsPojo teacher = getEntity(NodeAction.CREATE, Teacher.class, "1")
                .withProperty("fields",  hasItem(
                        getField(NodeType.PRIMITIVE, String.class, "name", "teacher", null)
                ));
        assertThat(audit.getModifications(), contains(teacher));
    }

    @Test
    public void embededBaseTeacherIncluded(){
        executeTx(pm -> {
            BaseTeacher mentor = new BaseTeacher("mentor");
            Teacher teacher = new Teacher("teacher", mentor);
            pm.makePersistent(teacher);
        });

        IsPojo teacher = getEntity(NodeAction.CREATE, Teacher.class, ANY )
                .withProperty("fields",  hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "name", "teacher", null),
                        getField(NodeType.REF, BaseTeacher.class, "mentor", ANY, null)
                ));
        assertThat(audit.getModifications(), contains(teacher));
    }
}
