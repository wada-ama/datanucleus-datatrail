package org.datanucleus.test;

import mydomain.model.BaseTeacher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class DatatrailExclusionTest extends AbstractTest{

    @Test
    public void baseTeacherExcluded(){
        executeTx(pm -> {
            BaseTeacher baseTeacher = new BaseTeacher("baseTeacher");
            pm.makePersistent(baseTeacher);
        });

        assertThat("No entities in audit trail", audit.getModifications(), Matchers.hasSize(0));
    }
}
