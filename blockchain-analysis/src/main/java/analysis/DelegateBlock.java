package analysis;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rpowell on 10/11/15.
 */
public class DelegateBlock {

    // The hash of the block
    private final Sha256Hash blockHash;

    // The transactions in the block
    private List<DelegateTransaction> transactions;

    /**
     * constructor.
     * @param block The block to create this delegate block from.
     */
    public DelegateBlock(Block block) {
        blockHash = block.getHash();
        transactions = createDelegateTransactions(block);
    }

    /**
     * Getter for delegate transactions.
     * @return The list of delegate transactions.
     */
    public List<DelegateTransaction> getTransactions() {
        return transactions;
    }

    /**
     * Extract the Transactions in the provided block and create delegate transactions from them.
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
