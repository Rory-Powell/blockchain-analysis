package data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rpowell on 10/11/15.
 */
public class DelegateBlock {

    @JsonProperty(value = "blockHash")
    private Sha256Hash blockHash;

    @JsonProperty(value = "transactions")
    private List<DelegateTransaction> transactions;

    public DelegateBlock() {
    }

    public DelegateBlock(Block block) {
        blockHash = block.getHash();
        transactions = createDelegateTransactions(block);
    }

    public DelegateBlock(String blockHash, List<DelegateTransaction> transactions) {
        this.blockHash = new Sha256Hash(blockHash);
        this.transactions = transactions;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = new Sha256Hash(blockHash);
    }

    public void setTransactions(List<DelegateTransaction> transactions) {
        this.transactions = transactions;
    }

    public String getBlockHash() {
        return blockHash.toString();
    }

    public List<DelegateTransaction> getTransactions() {
        return transactions;
    }

    /**
     * Extract the Transactions in the provided block and create delegate transactions from them.
     *
     * @param block The source block
     * @return A list of delegate transactions for the block.
     */
    private List<DelegateTransaction> createDelegateTransactions(Block block) {
        List<DelegateTransaction> delegateTransactions = null;
        List<Transaction> transactions = block.getTransactions();

        if (transactions != null && !transactions.isEmpty()) {
            delegateTransactions = block.getTransactions().stream()
                    .map(DelegateTransaction::new)
                    .collect(Collectors.toList());
        }
        return delegateTransactions;
    }
}
