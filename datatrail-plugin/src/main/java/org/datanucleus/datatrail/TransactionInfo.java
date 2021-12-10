package org.datanucleus.datatrail;

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
    Instant getDateModified();

    String getUsername();

    String getTxId();
}
