package analysis;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;

/**
 * Created by rpowell on 10/11/15.
 */
public class DelegateTransaction {

    Sha256Hash transactionHash;

    /**
     * Constructor.
     * @param transaction The transaction to create this delegate transaction from.
     */
    public DelegateTransaction(Transaction transaction) {
        transactionHash = transaction.getHash();
    }

}