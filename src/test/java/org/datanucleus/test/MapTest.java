package org.datanucleus.test;

import com.google.common.collect.Lists;
import com.spotify.hamcrest.pojo.IsPojo;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeType;
import mydomain.model.CountryCode;
import mydomain.model.QCountryCode;
import mydomain.model.QStudent;
import mydomain.model.Student;
import mydomain.model.Telephone;
import mydomain.model.TelephoneType;
import org.datanucleus.enhancement.Persistable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static mydomain.datanucleus.datatrail.NodeAction.CREATE;
import static mydomain.datanucleus.datatrail.NodeAction.DELETE;
import static mydomain.datanucleus.datatrail.NodeAction.UPDATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.SAME_THREAD)
public class MapTest extends AbstractTest {


    @Test
    public void createMap() throws IOException {
        executeTx(pm -> {

            CountryCode cc = new CountryCode("canada", 1);

            Student student = new Student("student");
            student.addTelephoneNb(TelephoneType.HOME, new Telephone("514-555-5555", cc));
            student.addMark("english", "A");

            pm.makePersistent(student);
        });

        final IsPojo<Node> student = getEntity(CREATE, Student.class, "1")
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "name", "student", null),
                        getContainerField(NodeType.MAP, "telephoneNbs")
                            .withProperty("added", hasItem(
                                    getMapElement(NodeType.PRIMITIVE, TelephoneType.class, TelephoneType.HOME.toString(), NodeType.REF, Telephone.class, "1", null)
                            ))
                ));


        final IsPojo<Node> cc = getEntity(CREATE, CountryCode.class, "1")
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "country", "canada", null),
                        getField(NodeType.PRIMITIVE, Integer.class, "code", "1", null)
                ));

        final IsPojo<Node> telephone = getEntity(CREATE, Telephone.class, "1")
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "number", "514-555-5555", null),
                        getField(NodeType.REF, CountryCode.class, "countryCode", "1", null)
                ));


        Collection<Node> entities = audit.getModifications();
        assertThat(entities, hasItem(cc));
        assertThat(entities, hasItem(telephone));
        assertThat(filterEntity(entities, Student.class, CREATE).get(), is(student));
        assertThat(entities, hasItem(student));
        assertThat(entities, containsInAnyOrder(student, cc, telephone));
    }


    @DisplayName("Deletes an Entity containing a Map object.")
    @Test
    public void deleteMap() throws IOException {
        Map<TelephoneType, String> ids = new HashMap<>();

        executeTx(pm -> {
            CountryCode cc = new CountryCode("canada", 1);

            Student student = new Student("student");
            student.addTelephoneNb(TelephoneType.HOME, new Telephone("514-555-5555", cc));
            student.addTelephoneNb(TelephoneType.MOBILE, new Telephone("514-777-5555", cc));
            student.addMark("english", "A");
            student.addMark("french", "A+");

            pm.makePersistent(student);

            ids.put( TelephoneType.HOME, getId((Persistable) student.getTelephoneNbs().get(TelephoneType.HOME)));
            ids.put( TelephoneType.MOBILE, getId((Persistable) student.getTelephoneNbs().get(TelephoneType.MOBILE)));
        }, false);


        // test removal of map members
        executeTx(pm -> {
            // find the student
            Student student = pm.newJDOQLTypedQuery(Student.class).filter(QStudent.candidate().name.eq("student")).executeUnique();
            pm.deletePersistent(student);
        });


        final IsPojo<Node> student = getEntity(DELETE, Student.class, "1")
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "name", "student", null),
                        getContainerField(NodeType.MAP, "telephoneNbs")
                            .withProperty("removed", hasItems(
                                    getMapElement(NodeType.PRIMITIVE, TelephoneType.class, TelephoneType.HOME.toString(), NodeType.REF, Telephone.class, ids.get(TelephoneType.HOME), null),
                                    getMapElement(NodeType.PRIMITIVE, TelephoneType.class, TelephoneType.MOBILE.toString(), NodeType.REF, Telephone.class, ids.get(TelephoneType.MOBILE), null)
                            )),
                        getContainerField(NodeType.MAP, "marks")
                            .withProperty("removed", hasItems(
                                    getMapElement(NodeType.PRIMITIVE, String.class, "english", NodeType.PRIMITIVE, String.class, "A", null),
                                    getMapElement(NodeType.PRIMITIVE, String.class, "french", NodeType.PRIMITIVE, String.class, "A+", null)
                            ))
                ));


        Collection<Node> entities = audit.getModifications();
        assertThat(entities, hasItem(student));
        assertThat("Should show an entity with a Map containing removed items", entities, contains(student));
    }


    @DisplayName("Updates the Key/Value pairs of a Map<Prim,Prim> map")
    @Test
    public void updateMapPrim() throws IOException {
        Map<TelephoneType, String> ids = new HashMap<>();

        executeTx(pm -> {
            CountryCode cc = new CountryCode("canada", 1);

            Student student = new Student("student");
            student.addTelephoneNb(TelephoneType.HOME, new Telephone("514-555-5555", cc));
            student.addTelephoneNb(TelephoneType.MOBILE, new Telephone("514-777-5555", cc));
            student.addMark("english", "A");
            student.addMark("french", "A+");

            pm.makePersistent(student);

            ids.put( TelephoneType.HOME, getId((Persistable) student.getTelephoneNbs().get(TelephoneType.HOME)));
            ids.put( TelephoneType.MOBILE, getId((Persistable) student.getTelephoneNbs().get(TelephoneType.MOBILE)));
        }, false);


        // test removal and change of map members
        executeTx(pm -> {
            // find the student
            Student student = pm.newJDOQLTypedQuery(Student.class).filter(QStudent.candidate().name.eq("student")).executeUnique();
            student.addMark("german", "B");
            student.getMarks().remove("french");
            student.addMark("english", "B");
        });


        final IsPojo<Node> student = getEntity(UPDATE, Student.class, "1")
                .withProperty("fields", hasItems(
                        getContainerField(NodeType.MAP, "marks")
                            .withProperty("added", hasItems(
                                    getMapElement(NodeType.PRIMITIVE, String.class, "german", NodeType.PRIMITIVE, String.class, "B", null)
                            ))
                            .withProperty("removed", hasItems(
                                    getMapElement(NodeType.PRIMITIVE, String.class, "french", NodeType.PRIMITIVE, String.class, "A+", null)
                            ))
                            .withProperty("changed", hasItems(
                                    getMapElement(NodeType.PRIMITIVE, String.class, "english", NodeType.PRIMITIVE, String.class, "B", "A")
                            ))
                ));


        Collection<Node> entities = audit.getModifications();
        assertThat(entities, hasItem(student));
        assertThat(entities, contains(student));
    }


    @DisplayName("Updates the Key/Value pairs of a Map<Prim,Ref> map")
    @Test
    public void updateMapRef() throws IOException {
        Map<TelephoneType, String> ids = new HashMap<>();

        executeTx(pm -> {
            CountryCode cc = new CountryCode("canada", 1);

            Student student = new Student("student");
            student.addTelephoneNb(TelephoneType.HOME, new Telephone("514-555-5555", cc));
            student.addTelephoneNb(TelephoneType.MOBILE, new Telephone("514-777-5555", cc));

            pm.makePersistent(student);

            ids.put( TelephoneType.HOME, getId((Persistable) student.getTelephoneNbs().get(TelephoneType.HOME)));
            ids.put( TelephoneType.MOBILE, getId((Persistable) student.getTelephoneNbs().get(TelephoneType.MOBILE)));
        }, false);


        // test removal and change of map members
        executeTx(pm -> {

            CountryCode cc = new CountryCode("USA", 1);

            // find the student
            Student student = pm.newJDOQLTypedQuery(Student.class).filter(QStudent.candidate().name.eq("student")).executeUnique();
            student.getTelephoneNbs().put(TelephoneType.MOBILE, new Telephone("514-555-9999", cc));
        });

        final IsPojo<Node> telephone = getEntity(CREATE, Telephone.class, ANY)
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "number", "514-555-9999", null),
                        getField(NodeType.REF, CountryCode.class, "countryCode", ANY, null)
                ));


        final IsPojo<Node> student = getEntity(UPDATE, Student.class, "1")
                .withProperty("fields", hasItems(
                        getContainerField(NodeType.MAP, "telephoneNbs")
                            .withProperty("changed", hasItems(
                                    getMapElement(NodeType.PRIMITIVE, TelephoneType.class, TelephoneType.MOBILE.toString(), NodeType.REF, Telephone.class, ANY, ids.get(TelephoneType.MOBILE))
                            ))
                ));

        final IsPojo<Node> usa = getEntity(CREATE, CountryCode.class, ANY)
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "country", "USA", null)
                ));


        Collection<Node> entities = audit.getModifications();
        assertThat(filterEntity(entities, CountryCode.class, CREATE).get(), is(usa));
        assertThat(entities, hasItem(usa));

        assertThat(filterEntity(entities, Student.class, UPDATE).get(), is(student));
        assertThat(entities, hasItem(student));
        assertThat(entities, containsInAnyOrder(student,usa, telephone));

    }


}
