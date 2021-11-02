package mydomain.datanucleus.datatrail;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Class to identify global transaction details to be found in the Data Trail
 *
 * Expected to find the TransactionInfo class in the {@link javax.jdo.PersistenceManager#getUserObject(Object)} location, with
 * key = {@code TransactionInfo.class.getName()}
 *
 * @author Eric Benzacar
 */
public class TransactionInfo implements Serializable {
    protected Instant dateModified;
    protected String username;
    protected String txId;

    public TransactionInfo(Instant dateModified, String username) {
        this.dateModified = dateModified;
        this.username = username;
        this.txId = UUID.randomUUID().toString();
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public String getUsername() {
        return username;
    }

    public String getTxId() {
        return txId;
    }
}
