package analysis;

import data.DelegateBlock;
import data.DelegateTransaction;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.FileSystemHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockAnalyser {

    private static final Logger log = LoggerFactory.getLogger(BlockAnalyser.class);
    private static Wallet wallet = new Wallet(new MainNetParams());
    private List<DelegateBlock> blocks = null;
    private List<DelegateTransaction> allTransactions = null;
    private FileSystemHandler fileSystemHandler = new FileSystemHandler();

    public BlockAnalyser() {

    }

    public Map<Long, Integer> mapTransactionsToTime(List<Block> blocks) {
        Map<Long, Integer> timeToTransactionCount = new TreeMap<>();

        for(Block block : blocks) {
            Long time = block.getTimeSeconds();

            Integer transactionCount;
            List<Transaction> transactions = block.getTransactions();
            if (transactions != null) {
                transactionCount = transactions.size();
            }
            else {
                transactionCount = 0;
            }

            timeToTransactionCount.put(time, transactionCount);
        }

        return timeToTransactionCount;
    }

    public List<Transaction> getTransactions(List<Block> blocks) {
        List<Transaction> transactions = new ArrayList<>();
        for(Block block : blocks) {
            List<Transaction> currentTransactions = block.getTransactions();

            if(currentTransactions != null) {
                transactions.addAll(currentTransactions);
            }
        }
        return transactions;
    }

    public List<Block> parseBlocksOriginalFormat() {
        return fileSystemHandler.parseBlockOriginalFormat();
    }

    public void parseAndWriteBlocks() {
        fileSystemHandler.parseAndWriteBlocks();
    }

    public void readBlocksFromDisk() {
        blocks = fileSystemHandler.readBlocks();
    }

    public List<DelegateBlock> getBlocks() {
        if (blocks == null) {
            log.info("Blocks are null. Perhaps you need to readBlocksFromDisk() first.");
        }
        return blocks;
    }

    public List<DelegateTransaction> getAllTransactions() {
        if (allTransactions == null) {
            for (DelegateBlock block : getBlocks()) {
                allTransactions.addAll(block.getTransactions().stream().collect(Collectors.toList()));
            }
        }
        return allTransactions;
    }

    public double averageTransactionCountPerBlock() {
        double totalTransactionCount = allTransactions.size();
        double numberOfBlocks = blocks.size();
        return totalTransactionCount / numberOfBlocks;
    }
}
