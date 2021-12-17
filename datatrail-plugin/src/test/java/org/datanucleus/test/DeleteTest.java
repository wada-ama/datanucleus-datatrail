package org.datanucleus.test;

import org.datanucleus.test.model.CountryCode;
import org.datanucleus.test.model.QCountryCode;
import org.datanucleus.test.model.Telephone;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class DeleteTest extends AbstractTest{

    @DisplayName("Use of lazy fields in deleted objects should not cause exceptions")
    @Test
    void testLazyLoadedFields(){
        // remove a FK constraint to allow for inconsistent deletes
        executeTx( pm -> {
            pm.newQuery("javax.jdo.query.SQL", "ALTER TABLE TELEPHONE DROP CONSTRAINT TELEPHONE_FK1").execute();

        },false);


        // create data
        executeTx(pm -> {
            CountryCode can = new CountryCode("can", 1);
            CountryCode usa = new CountryCode("usa", 1);

            Telephone telephone = new Telephone( "12345", can);
            pm.makePersistentAll(usa, telephone);

        }, false);


        // triggers an error because LazyLoaded fields are required after the object has been marked as deleted
        executeTx(pm -> {
            pm.getFetchGroup(CountryCode.class, "lazy").addMember("country");
            pm.getFetchPlan().clearGroups().addGroup("lazy");

            Telephone telephone = pm.newJDOQLTypedQuery(Telephone.class).executeUnique();
            CountryCode can = telephone.getCountryCode();
            pm.deletePersistent(can);

            CountryCode usa = pm.newJDOQLTypedQuery(CountryCode.class).filter(QCountryCode.candidate().country.eq("usa")).executeUnique();
            telephone.setCountryCode(usa);
        });

        MatcherAssert.assertThat(audit.getModifications(), Matchers.hasSize(2));
    }

}
