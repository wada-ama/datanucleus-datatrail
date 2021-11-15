package org.datanucleus.test;

import com.spotify.hamcrest.pojo.IsPojo;
import mydomain.datanucleus.datatrail.Node;
import mydomain.datanucleus.datatrail.NodeAction;
import mydomain.datanucleus.datatrail.NodeType;
import org.datanucleus.test.model.CountryCode;
import org.datanucleus.test.model.QTelephone;
import org.datanucleus.test.model.Telephone;
import org.datanucleus.identity.DatastoreIdImplKodo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;


public class ReferenceTest extends AbstractTest {

    @DisplayName("Create reference object using parent object commit only.  Expect to see both objects in DT")
    @Test
    public void createReference() throws IOException {
        executeTx(pm -> {
            CountryCode canada = new CountryCode("Canada", 1);
            Telephone telephone = new Telephone("514-123-1234", canada);
            pm.makePersistent(telephone);
        });

        Collection<Node> entities = audit.getModifications();

        final IsPojo<Node>countryCode = getEntity(NodeAction.CREATE, CountryCode.class, "1")
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "country", "Canada", null),
                        getField(NodeType.PRIMITIVE, Integer.class, "code", "1", null)
                ));

        assertThat(entities, hasItem(countryCode));

        final IsPojo<Node>telephone = getEntity(NodeAction.CREATE, Telephone.class, "1")
                .withProperty( "fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "number", "514-123-1234", null),
                        getField(NodeType.REF, CountryCode.class, "countryCode", "1", null)
                ));

        assertThat(entities, hasItem(telephone));

        // final check
        assertThat(entities, containsInAnyOrder(telephone, countryCode));
    }



    @Test
    public void deleteReference() throws IOException {
        executeTx(pm -> {
            CountryCode canada = new CountryCode("Canada", 1);
            Telephone telephone = new Telephone("514-123-1234", canada);

            CountryCode usa = new CountryCode( "USA", 1);
            pm.makePersistent(telephone);
            pm.makePersistent(usa);
        }, false);


        executeTx(pm -> {
            Telephone telephone = pm.newJDOQLTypedQuery(Telephone.class).executeUnique();
            pm.deletePersistent(telephone);
        });

        Collection<Node> entities = audit.getModifications();

        final IsPojo<Node>telephone = getEntity(NodeAction.DELETE, Telephone.class, "1")
                .withProperty("fields", hasItems(
                        getField(NodeType.PRIMITIVE, String.class, "number", "514-123-1234", null),
                        getField(NodeType.REF, CountryCode.class, "countryCode", "1", null)
                ));

        assertThat(entities, containsInAnyOrder(telephone));
    }


    @Test
    public void updateReference() throws IOException {
        executeTx(pm -> {
            CountryCode canada = new CountryCode("Canada", 1);
            Telephone telephone = new Telephone("514-123-1234", canada);

            CountryCode usa = new CountryCode( "USA", 1);
            pm.makePersistent(telephone);
            pm.makePersistent(usa);
        }, false);


        executeTx(pm -> {
            Object id = new DatastoreIdImplKodo(CountryCode.class.getName(), 2);
            CountryCode usa = pm.getObjectById(CountryCode.class, id);

            id = new DatastoreIdImplKodo(Telephone.class.getName(), 1);
            Telephone telephone = pm.getObjectById(Telephone.class, id);
            telephone.setCountryCode(usa);
        });

        Collection<Node> entities = audit.getModifications();

        final IsPojo<Node>telephone = getEntity(NodeAction.UPDATE, Telephone.class, "1")
                .withProperty( "fields", hasItems(
                        getField(NodeType.REF, CountryCode.class, "countryCode", "2", "1")
                ));

        assertThat(entities, containsInAnyOrder(telephone));
    }



    @DisplayName("Changing an existing reference to NULL should only show prevValue for the Ref")
    @Test
    public void updateReferenceToNull() {
        executeTx(pm -> {
            CountryCode canada = new CountryCode("Canada", 1);
            Telephone telephone = new Telephone("514-123-1234", canada);

            CountryCode usa = new CountryCode( "USA", 1);
            pm.makePersistent(telephone);
            pm.makePersistent(usa);
        }, false);


        executeTx(pm -> {
            Telephone telephone = pm.newJDOQLTypedQuery(Telephone.class).filter(QTelephone.candidate().number.endsWith("1234")).executeUnique();
            telephone.setCountryCode(null);
        });

        Collection<Node> entities = audit.getModifications();

        final IsPojo<Node>telephone = getEntity(NodeAction.UPDATE, Telephone.class, "1")
                .withProperty( "fields", hasItems(
                        getField(NodeType.REF, CountryCode.class, "countryCode", null, "1")
                ));

        assertThat(entities, containsInAnyOrder(telephone));
    }


}
