package org.datanucleus.datatrail.spi;

import java.io.Serializable;
import java.time.Instant;

/**
 * Class to identify global transaction details to be found in the Data Trail
 *
 * Expected to find the TransactionInfo class in the {@link javax.jdo.PersistenceManager#getUserObject(Object)} location, with
 * key = {@code TransactionInfo.class.getName()}
 *
 * @author Eric Benzacar
 */
public interface TransactionInfo extends Serializable {

    /**
     * Gets the date the transaction occurred
     * @return
     */
    Instant getTxDate();

    /**
     * Gets the name of the user that triggered the transaction
     * @return
     */
    String getUserId();

    /**
     * Gets a unique transaction identifier
     * @return
     */
    String getTxId();
}
