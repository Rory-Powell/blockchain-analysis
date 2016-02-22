package util;

import domain.Transaction;
import org.bitcoinj.core.*;
import org.bitcoinj.script.ScriptChunk;
import org.bitcoinj.utils.BlockFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockParser {
    private static final Logger log = LoggerFactory.getLogger(BlockParser.class);
    private NetworkParameters networkParameters;
    private Context context;

    // Constructor
    public BlockParser(NetworkParameters netParams) {
        networkParameters = netParams;
        context = new Context(networkParameters);
    }

    public void parseBlockFiles(List<File> files) {
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, files);

        for (Block block : blockFileLoader) {
            log.info("Parsed block " + block.getHashAsString());

            Set<Transaction> transactions = new HashSet<>();
            if (block.getTransactions() != null) {
                for (org.bitcoinj.core.Transaction transaction : block.getTransactions()) {
                    Transaction simpleTransaction = new Transaction();
                    simpleTransaction.setInputs(extractInputsFromTransaction(transaction));
                    simpleTransaction.setOutputs(extractOutputsFromTransaction(transaction));
                    transactions.add(simpleTransaction);
                }
            }
        }
    }

    // Extract inputs from transaction.
    private Set<String> extractInputsFromTransaction(org.bitcoinj.core.Transaction transaction) {
        Set<String> inputs = new HashSet<>();
        for (TransactionInput input : transaction.getInputs()) {
            try {
                List<ScriptChunk> chunks = input.getScriptSig().getChunks();
                if (checkChunks(chunks)) {
                    if (input.isCoinBase()) {
                        inputs.add("COINBASE");

                    } else {
                        inputs.add(input.getFromAddress().toString());
                    }
                }
            } catch (ScriptException e) {
                log.error("ScriptException", e);
            }
        }
        return inputs;
    }

    // Extract outputs from a transaction.
    private Set<String> extractOutputsFromTransaction(org.bitcoinj.core.Transaction transaction) {
        Set<String> outputs = new HashSet<>();
        for (TransactionOutput output : transaction.getOutputs()) {
            try {
                outputs.add(output.getScriptPubKey()
                                    .getToAddress(networkParameters, true)
                                    .toString());
            } catch (ScriptException e) {
                log.error("Script exception", e);
            }
        }
        return outputs;
    }

    // Warning: lots of hacking.
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

    public static void logTiming(long startTimeMill) {
        long endTimeMill = System.currentTimeMillis();
        long parseTimeSec = TimeUnit.MILLISECONDS.toSeconds(endTimeMill - startTimeMill);
        log.info("Parse took " + parseTimeSec + " seconds");
    }
}
