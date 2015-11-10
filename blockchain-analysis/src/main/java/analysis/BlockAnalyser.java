package analysis;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.BlockChainParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockAnalyser {

    public static final String DEFAULT_PATH="/home/rpowell/dev/resources/blocks/5";
    private static final Logger log = LoggerFactory.getLogger(BlockAnalyser.class);
    private BlockChainParser blockChainParser;
    private List<Block> blocks;
    private List<Transaction> transactions;
    private Wallet wallet = new Wallet(new MainNetParams());

    public BlockAnalyser() {
        this(DEFAULT_PATH);
    }

    public BlockAnalyser(String path) {
        blockChainParser = new BlockChainParser(path);
        blocks = blockChainParser.parseBlocks();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public double averageTransactionCountPerBlock() {
        double totalTransactionCount = getTransactions().size();
        double numberOfBlocks = blocks.size();
        return totalTransactionCount / numberOfBlocks;
    }

    public double averageTransactionAmountPerBlock() {
        List<Transaction> allTransactions = getTransactions();
        double numberOfBlocks = blocks.size();
        double totalTransactionAmount = 0;

        for (Transaction transaction : allTransactions) {
            double transactionAmount = transaction.getValue(wallet).getValue();
            totalTransactionAmount = totalTransactionAmount + transactionAmount;
        }
        return totalTransactionAmount / numberOfBlocks;
    }

    /**
     * Get a count of blocks by month.
     * @return A map, keyed by month name to block count.
     */
    public Map<String, Integer> getMonthlyBlockCount() {
        Map<String, Integer> monthlyBlockCount = new HashMap<>();

        for(Block block : blocks) {
            // Extract the month keyword.
            String month = new SimpleDateFormat("yyyy-MM").format(block.getTime());

            // Make sure there exists an entry for the extracted month.
            if (!monthlyBlockCount.containsKey(month)) {
                monthlyBlockCount.put(month, 0);
            }
            monthlyBlockCount.put(month, 1 + monthlyBlockCount.get(month));
        }
        return monthlyBlockCount;
    }

    /**
     * Get all the transactions this block analyser is aware of.
     * @return A list of the transactions.
     */
    private List<Transaction> getTransactions() {
        if (transactions == null) {
            List<Transaction> blockTransactions;
            List<Transaction> allTransactions = new ArrayList<>();

            for (Block block : blocks) {
                blockTransactions = block.getTransactions();
                if (blockTransactions != null && !blockTransactions.isEmpty()) {
                    allTransactions.addAll(blockTransactions);
                }
            }
            transactions = allTransactions;
        }
        return transactions;
    }
}
