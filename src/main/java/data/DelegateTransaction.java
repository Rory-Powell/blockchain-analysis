package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;

/**
 * Created by rpowell on 10/11/15.
 */
public class DelegateTransaction {

    @JsonProperty(value="transactionHash")
    private Sha256Hash transactionHash;

    public DelegateTransaction() {
    }

    public DelegateTransaction(Transaction transaction) {
        transactionHash = transaction.getHash();
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = new Sha256Hash(transactionHash);
    }

    public String getTransactionHash() {
        return transactionHash.toString();
    }
}