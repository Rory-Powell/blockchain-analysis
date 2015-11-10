package analysis;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.BlockChainParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpowell on 08/11/15.
 * TODO use predetermined block file in test directory setting known values on tests.
 */
public class TransactionAnalyser {

    public static final String DEFAULT_PATH="/home/rpowell/dev/resources/blocks/1";
    private static final Logger log = LoggerFactory.getLogger(TransactionAnalyser.class);
    private Wallet wallet = new Wallet(new MainNetParams());

    private List<DelegateBlock> blocks;
    private List<DelegateTransaction> allTransactions;

    public TransactionAnalyser() {
        this(DEFAULT_PATH);
    }

    public TransactionAnalyser(String path) {
        BlockChainParser blockChainParser = new BlockChainParser(path);
        blocks = blockChainParser.getBlocks();
        allTransactions = new ArrayList<>();
        blocks.stream().forEach(block -> allTransactions.addAll(block.getTransactions()));
    }

    public List<DelegateBlock> getBlocks() {
        return blocks;
    }

    public double averageTransactionCountPerBlock() {
        double totalTransactionCount = allTransactions.size();
        double numberOfBlocks = blocks.size();
        return totalTransactionCount / numberOfBlocks;
    }

    // TODO implement getvalue with delegate transaction
    public double averageTransactionAmountPerBlock() {
        double numberOfBlocks = blocks.size();
        double totalTransactionAmount = 0;

        for (DelegateTransaction transaction : allTransactions) {
//            double transactionAmount = transaction.getValue(wallet).getValue();
//            totalTransactionAmount = totalTransactionAmount + transactionAmount;
        }
        return totalTransactionAmount / numberOfBlocks;
    }

    /**
     * Getter for all transactions.
     * @return The composite list of transactions.
     */
    private List<DelegateTransaction> getAllTransactions() {
        return allTransactions;
    }
}
