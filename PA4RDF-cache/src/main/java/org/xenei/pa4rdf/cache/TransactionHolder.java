package org.xenei.pa4rdf.cache;

import org.apache.jena.query.ReadWrite;
import org.apache.jena.sparql.core.Transactional;

public class TransactionHolder {
    private final Transactional txn;
    private final boolean started;
    public TransactionHolder(Transactional txn, ReadWrite rw) {
        this.txn = txn;
        started = !txn.isInTransaction();
        if (started) {
            txn.begin( rw );
        }
    }

    public boolean ownsTranaction() {
        return started;
    }

    public void commit() {
        if (started) {
            txn.commit();
        }
    }

    public void abort() {
        if (started) {
            txn.abort();
        }
    }

    public void end() {
        if (started) {
            txn.end();
        }
    }

}
