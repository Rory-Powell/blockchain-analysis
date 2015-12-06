package parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.DelegateBlock;
import data.SimpleTransaction;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptChunk;
import org.bitcoinj.utils.BlockFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockParser {

    private static final Logger log = LoggerFactory.getLogger(BlockParser.class);

    // Parameters for connecting to the bitcoin network
    private NetworkParameters networkParameters;

    // The configuration context, stashed in thread local storage (TLS)
    private Context context;

    private ObjectMapper mapper = new ObjectMapper();
    private int fileNameIncrement = 0;

    public static final String TAB_PATH = "/home/rpowell/dev/projects/final-project/";

    /**
     * Constructor.
     */
    public BlockParser(NetworkParameters netParams) {
        networkParameters = netParams;
        context = new Context(networkParameters);
    }

    public List<Block> parseBlocksOriginalFormat(List<File> files) throws IOException {
        List<Block> blocks = new ArrayList<>();
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, files);

        StringBuilder stringBuilder;
        BufferedWriter bufferedWriter;
        int count = 0;

        bufferedWriter = new BufferedWriter(new FileWriter(new File(TAB_PATH + "blockchain_169_full_coinbase.csv")));

        // For all found blocks
        for (Block block : blockFileLoader) {
            log.info("Parsed block " + block.getHashAsString());

            Set<SimpleTransaction> simpleTransactions = new HashSet<>();

            stringBuilder = new StringBuilder();
            if (block.getTransactions() != null) {

                for (Transaction transaction : block.getTransactions()) {

                    List<TransactionInput> transactionInputs = transaction.getInputs();
                    List<TransactionOutput> transactionOutputs = transaction.getOutputs();

                    SimpleTransaction simpleTransaction = new SimpleTransaction();

                    // For all inputs in the transaction
                    for (TransactionInput input : transactionInputs) {
                        try {
                            List<ScriptChunk> chunks = input.getScriptSig().getChunks();

                            if (checkChunks(chunks)) {
                                if (input.isCoinBase()) {
//                                    simpleTransaction.addInput("COINBASE");

                                } else {
                                    simpleTransaction.addInput(input.getFromAddress().toString());
                                }
                            }
                        }
                        catch (ScriptException e) {
                            log.error("ScriptException", e);
                        }
                    }

                    // For all outputs in the transaction
                    for (TransactionOutput output : transactionOutputs) {
                        try {
                            simpleTransaction.addOutput(output.getScriptPubKey().getToAddress(networkParameters, true).toString());
                        }
                        catch (ScriptException e) {
                            log.error("Script exception", e);
                        }
                    }

                    simpleTransactions.add(simpleTransaction);

                    log.info("Parsed transaction " + count);
                    count++;
                }

                // Now build csv string
                for (SimpleTransaction transaction : simpleTransactions) {
                    for (String input : transaction.getInputs()) {
                        if (transaction.getOutputs().size() > 0) {
                            stringBuilder.append(input);
                            for(String output : transaction.getOutputs()) {
                                stringBuilder.append(";").append(output);
                            }
                            stringBuilder.append("\n");
                        }
                    }
                }
            }

            try {
                bufferedWriter.write(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            blocks.add(block);
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }

    private boolean checkChunks(List<ScriptChunk> chunks) {

        boolean valid = false;
        if (chunks.size() != 2) { // pre check
            return valid;
        }

        final ScriptChunk chunk0 = chunks.get(0);
        final byte[] chunk0data = chunk0.data;
        final ScriptChunk chunk1 = chunks.get(1);
        final byte[] chunk1data = chunk1.data;

        if (chunk0data != null && chunk0data.length > 2 && chunk1data != null && chunk1data.length > 2) {
            valid = true;
        }

        return valid;
    }

    /**
     * Parse and write a list of files as json.
     * @param files The files to write.
     * @param path  The path to write files.
     */
    public void writeFilesAsBlocks(List<File> files, String path) {
        // Logging setup
        long startTimeMill = System.currentTimeMillis();
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, files);

        // Iterate over the blocks in the file.
        for (Block block : blockFileLoader) {
            DelegateBlock delegateBlock = new DelegateBlock(block);

            try {
                // Build file path
                String filePath = path.concat("/" + fileNameIncrement + ".json");
                fileNameIncrement++;

                // Write to disk
                File file = new File(filePath);
                log.info("Parsed block " + delegateBlock.getBlockHash() + ". Writing to disk as " + file.getName());
                mapper.writeValue(file, delegateBlock);
            } catch (IOException e) {
                log.error("IOException while writing file", e);
            }
        }

        logTiming(startTimeMill);
    }

    /**
     * Read files on a path as {@link DelegateBlock}'s
     * @param path  The path to read from
     * @return      The List of blocks.
     */
    public List<DelegateBlock> readFilesAsBlocks(String path) {
        long startTimeMill = System.currentTimeMillis();
        List<File> filesOnJsonPath = FileWalker.discoverFilesOnPath(path);
        List<DelegateBlock> blocks = new ArrayList<>();
        for (File file : filesOnJsonPath) {
            log.info("Reading file " + file);
            try {
                DelegateBlock block = mapper.readValue(file, DelegateBlock.class);
                blocks.add(block);
            } catch (IOException e) {
                log.error("IOException while reading file " + file, e);
            }
        }

        logTiming(startTimeMill);
        return blocks;
    }

    public static void logTiming(long startTimeMill) {
        long endTimeMill = System.currentTimeMillis();
        long parseTimeSec = TimeUnit.MILLISECONDS.toSeconds(endTimeMill - startTimeMill);
        log.info("Parse took " + parseTimeSec + " seconds");
    }
}
