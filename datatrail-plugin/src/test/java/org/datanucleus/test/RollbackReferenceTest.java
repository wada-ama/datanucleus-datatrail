package org.datanucleus.test;

import org.datanucleus.datatrail.TransactionListener;
import org.datanucleus.test.model.CountryCode;
import org.datanucleus.test.model.QCountryCode;
import org.datanucleus.test.model.Telephone;
import org.datanucleus.util.NucleusLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.jdo.JDODataStoreException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


class RollbackReferenceTest extends AbstractTest {

    @DisplayName("Deleting FK during tx trigger rollback")
    @Test
    void rollbackTestFlush() {
        executeTx(pm -> {
            CountryCode canada = new CountryCode("Canada", 1);
            Telephone telephone = new Telephone("514-123-1234", canada);
            pm.makePersistent(telephone);
        }, false);


        // Create of object
        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        TransactionListener txListener = new TransactionListener(entities -> {
            fail("Should not be calling the post-commit listener since commit should have failed");
        });
        txListener.attachListener(pm, null);

        AtomicBoolean rollback = new AtomicBoolean(false);
        assertThrows(JDODataStoreException.class, () -> {
            try {
                tx.begin();
                CountryCode usa = new CountryCode("USA", 1);
                pm.makePersistent(usa);
                pm.flush();


                CountryCode canada = pm.newJDOQLTypedQuery(CountryCode.class).filter(QCountryCode.candidate().country.eq("Canada")).executeUnique();
                pm.deletePersistent(canada);

                // force a flush to trigger the rollback
                pm.flush();

                tx.commit();
            } finally {
                if (tx.isActive()) {
                    NucleusLogger.GENERAL.error(">> Rolling back Tx");
                    tx.rollback();
                    rollback.set(true);
                }
                pm.close();
            }
        }, "Expecting exception to be thrown while trying to delete an FK");

        pmf.getDataStoreCache().evictAll();

        assertThat("Ensure transaction was rolled back since exception occured in middle of tx", rollback.get(), is(true));
        assertThat("Should be nothing in the DataTrail to show", audit.getModifications(), hasSize(0));
    }


    @DisplayName("Deleting FK during commit should not cause rollback")
    @Test
    void rollbackTestCommit() {
        executeTx(pm -> {
            CountryCode canada = new CountryCode("Canada", 1);
            Telephone telephone = new Telephone("514-123-1234", canada);
            pm.makePersistent(telephone);
        }, false);


        // Create of object
        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        TransactionListener txListener = new TransactionListener(entities -> {
            fail("Should not be calling the post-commit listener since commit should have failed");
        });
        txListener.attachListener(pm, null);

        AtomicBoolean rollback = new AtomicBoolean(false);
        assertThrows(JDODataStoreException.class, () -> {
            try {
                tx.begin();
                CountryCode usa = new CountryCode("USA", 1);
                pm.makePersistent(usa);
                pm.flush();


                CountryCode canada = pm.newJDOQLTypedQuery(CountryCode.class).filter(QCountryCode.candidate().country.eq("Canada")).executeUnique();
                pm.deletePersistent(canada);

                tx.commit();
            } finally {
                if (tx.isActive()) {
                    NucleusLogger.GENERAL.error(">> Rolling back Tx");
                    tx.rollback();
                    rollback.set(true);
                }
                pm.close();
            }
        }, "Expecting exception to be thrown while trying to delete an FK");
        pmf.getDataStoreCache().evictAll();

        assertThat("Rollback not required since TX not active", rollback.get(), is(false));
        assertThat("Should be nothing in the DataTrail to show", audit.getModifications(), hasSize(0));
    }


}
