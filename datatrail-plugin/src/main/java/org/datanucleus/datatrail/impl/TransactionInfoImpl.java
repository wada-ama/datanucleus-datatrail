package org.datanucleus.datatrail.impl;

import org.datanucleus.datatrail.TransactionInfo;

import javax.jdo.PersistenceManager;
import java.time.Instant;
import java.util.UUID;

/**
 * Default implementation to identify global transaction details to be found in the Data Trail
 * <p>
 * Expected to find an implementation of the {@link TransactionInfo} class in the {@link PersistenceManager#getUserObject(Object)} location, with
 * key = {@code TransactionInfo.class.getName()}
 * <p>
 *
 * @author Eric Benzacar
 */
public class TransactionInfoImpl implements TransactionInfo {

    private static final String NO_USERNAME = "__NOT_DEFINED__";

    protected final Instant dateModified;
    protected final String username;
    protected final String txId;


    public TransactionInfoImpl(final Instant dateModified) {
        this(dateModified, NO_USERNAME, UUID.randomUUID().toString());
    }

    public TransactionInfoImpl(final Instant dateModified, final String username, final String txId) {
        this.dateModified = dateModified;
        this.username = username;
        this.txId = txId;
    }

    @Override
    public Instant getDateModified() {
        return dateModified;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getTxId() {
        return txId;
    }
}
