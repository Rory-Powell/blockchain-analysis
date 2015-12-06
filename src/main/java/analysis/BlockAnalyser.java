package analysis;

import data.DelegateBlock;
import data.DelegateTransaction;
import data.SimpleTransaction;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.FileSystemHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private FileSystemHandler fileSystemHandler;
    private NetworkParameters netParams = new MainNetParams();

    /**
     *
     */
    public BlockAnalyser() {
        this.fileSystemHandler = new FileSystemHandler(netParams);
    }

    public void writeTabbedFiles(List<SimpleTransaction> simpleTransactions) throws IOException {
        fileSystemHandler.writeTabDelimitedTransactions(simpleTransactions);
    }

    /**
     * Iterate through a list of blocks and extract the from and to addresses from all transactions.
     * @return A list of {@link SimpleTransaction}'s.
     */
    public List<SimpleTransaction> createTransactions() throws IOException {
        return this.createTransactions(parseBlocksOriginalFormat());
    }

    /**
     * Iterate through a list of blocks and extract the from and to addresses from all transactions.
     * @param blocks A list of blocks.
     * @return A list of {@link SimpleTransaction}'s.
     */
    public List<SimpleTransaction> createTransactions(List<Block> blocks) throws IOException {

//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(TAB_PATH + "blockchain.txt")));
        StringBuilder stringBuilder = new StringBuilder();

        List<SimpleTransaction> simpleTransactions = new ArrayList<>();
        int count = 0;

        for (Block block : blocks) {

//            if (block.getTransactions() != null) {
//                for (Transaction transaction : block.getTransactions()) {
//                    List<String> inputs = new ArrayList<>();
//                    List<String> outputs = new ArrayList<>();
//
//                    for (TransactionInput input : transaction.getInputs()) {
//                            inputs.add(input.getScriptSig().toString());
//                    }
//
//                    for (TransactionOutput output : transaction.getOutputs()) {
//                        outputs.add(output.getScriptPubKey().toString());
//                    }
//
//                    for(String input : inputs) {
//                        for (String output : outputs) {
//                            stringBuilder.append(input).append("\t").append(output).append("\n");
//                        }
//                    }
//
//                    log.info("Parsed transaction " + count);
//                    count++;
//                }
//            }
        }

//        bufferedWriter.write(stringBuilder.toString());
//        bufferedWriter.close();
        return simpleTransactions;
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
