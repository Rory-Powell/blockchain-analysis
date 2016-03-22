package rpowell.blockchain.services;

import rpowell.blockchain.domain.PublicKey;
import rpowell.blockchain.Transaction;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.ScriptChunk;
import org.bitcoinj.utils.BlockFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpowell.blockchain.util.FileWalker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ParseServiceImpl implements IParseService {

    @Autowired
    IPublicKeyService publicKeyService;

    private static final Logger log = LoggerFactory.getLogger(IParseService.class);
    private NetworkParameters networkParameters = new MainNetParams();
    private Context context = new Context(networkParameters);

    private int blockCount = 0;

    public void parseBlockFiles() {
        this.parseBlockFiles(FileWalker.discoverFilesOnDefaultPath());
    }

    public void parseBlockFiles(List<File> files) {
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, files);

        for (Block block : blockFileLoader) {
            log.info("Parsed block " + block.getHashAsString());
            List<Transaction> blockTransactions = (mapTransactions(block.getTransactions()));

            for (Transaction transaction : blockTransactions) {
                writePublicKeysToDB(extractLinkedInputs(transaction));
            }

            // Temporary limiter
            blockCount++;
            if (blockCount == 51726) {
                break;
            }
        }
    }

    /**
     * Transform a list of bitcoinj transactions into domain format transactions.
     * @param transactions  The bitcoinj transactions.
     * @return              The domain transactions.
     */
    private List<Transaction> mapTransactions(List<org.bitcoinj.core.Transaction> transactions) {
        List<Transaction> mappedTransactions = new ArrayList<>();

        if (transactions != null && !transactions.isEmpty()) {
            for (org.bitcoinj.core.Transaction blockTransaction : transactions) {

                // Create a domain transaction
                Transaction transaction = new Transaction();
                transaction.setInputs(extractInputsFromTransaction(blockTransaction));
                transaction.setOutputs(extractOutputsFromTransaction(blockTransaction));

                mappedTransactions.add(transaction);
            }
        }

        return mappedTransactions;
    }

    private List<PublicKey> extractLinkedInputs(Transaction transaction) {
        List<PublicKey> linkedInputs = new ArrayList<>();

        // Extract the inputs, checking if any already exist
        List<PublicKey> inputs = new ArrayList<>();
        for (String key : transaction.getInputs()) {
            PublicKey existingInput = publicKeyService.findByKey(key);

            if (existingInput != null) {
                inputs.add(existingInput);
            } else {
                inputs.add(new PublicKey(key));
            }
        }

        // Extract the outputs, checking if any already exist
        List<PublicKey> outputs = new ArrayList<>();
        for (String key : transaction.getOutputs()) {
            PublicKey existingOutput = publicKeyService.findByKey(key);

            if (existingOutput != null) {
                outputs.add(existingOutput);
            } else {
                outputs.add(new PublicKey(key));
            }
        }

        // Link each input to each output
        for (PublicKey input : inputs) {
            for (PublicKey output : outputs) {
                // Check for sending change back to original address, and negate it
                if (!Objects.equals(input.getPublicKey(), output.getPublicKey())) {
                    input.linkAddress(output);
                }
            }

            linkedInputs.add(input);
        }

        return linkedInputs;
    }

    /**
     * Write a public key to the database. This will also persist the linked
     * keys and the relationship between the.
     * @param publicKey The key to save.
     */
    private void writePublicKeyToDB(PublicKey publicKey) {
        if (publicKey != null) {
            publicKeyService.saveKey(publicKey);
        }
    }

    /**
     * Save a list of public keys to the database. This will also persist the linked
     * keys and the relationship between them.
     * @param publicKeys The keys to save.
     */
    private void writePublicKeysToDB(List<PublicKey> publicKeys) {
        if (publicKeys != null && !publicKeys.isEmpty()) {
            publicKeyService.saveAllKeys(publicKeys);
        }
    }

    // Extract inputs from transaction.
    private List<String> extractInputsFromTransaction(org.bitcoinj.core.Transaction transaction) {
        List<String> inputs = new ArrayList<>();
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
                log.error("Script exception", e);
            }
        }
        return inputs;
    }

    // Extract outputs from a transaction.
    private List<String> extractOutputsFromTransaction(org.bitcoinj.core.Transaction transaction) {
        List<String> outputs = new ArrayList<>();
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
}
