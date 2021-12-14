package org.datanucleus.datatrail.impl;

import org.datanucleus.datatrail.spi.TransactionInfo;

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

    protected final Instant txDate;
    protected final String userId;
    protected final String txId;


    public TransactionInfoImpl(final Instant txDate) {
        this(txDate, NO_USERNAME, UUID.randomUUID().toString());
    }

    public TransactionInfoImpl(final Instant txDate, final String userId, final String txId) {
        this.txDate = txDate;
        this.userId = userId;
        this.txId = txId;
    }

    @Override
    public Instant getTxDate() {
        return txDate;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getTxId() {
        return txId;
    }
}
