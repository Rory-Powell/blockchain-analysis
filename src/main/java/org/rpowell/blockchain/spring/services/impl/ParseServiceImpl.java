package org.rpowell.blockchain.spring.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.Input;
import org.rpowell.blockchain.domain.Output;
import org.rpowell.blockchain.domain.Transaction;
import org.rpowell.blockchain.util.file.FileComparator;
import org.rpowell.blockchain.spring.controllers.GraphController;
import org.rpowell.blockchain.spring.services.IParseService;
import org.rpowell.blockchain.util.file.FileUtil;
import org.rpowell.blockchain.util.constant.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.rpowell.blockchain.util.graph.GraphConstants.*;

@Service
public class ParseServiceImpl implements IParseService {

    private static final Logger log = LoggerFactory.getLogger(IParseService.class);
    private final double SATOSHI = 0.00000001;
    private final ObjectMapper mapper = new ObjectMapper();

    private boolean isShutdown = false;

    private Map<String, Long> addressIndex = new HashMap<>();

    protected ParseServiceImpl() {}

    @Override
    public void writeJSONToDB() {
        BatchInserter batchInserter = null;
        try {
            List<File> jsonFiles = FileUtil.getFolderContents(StringConstants.JSON_PATH);
            // Sort the files from earliest to latest
            Collections.sort(jsonFiles, new FileComparator(StringConstants.JSON_FILE_EXT));

            List<File> nonPersistedFiles;

            // If there have been blocks previously downloaded / persisted - get the new ones
            if (FetcherServiceImpl.latestBlockOnDisk != null) {
                // Get the previously stored latest block on disk
                int latestBlockIndex = (int) FetcherServiceImpl.latestBlockOnDisk.getBlock_index();

                // Find the index of that block (index in the list, not the index in the blockchain)
                int fromIndex = -1;
                for (int i = 0; i < jsonFiles.size(); i++) {
                    File file = jsonFiles.get(i);
                    if (file.getName().substring(0, file.getName().length() - StringConstants.JSON_FILE_EXT.length())
                                        .equals(Integer.toString(latestBlockIndex))) {
                        fromIndex = i;
                        break; // Found it
                    }
                }

                nonPersistedFiles = jsonFiles.subList(fromIndex + 1, jsonFiles.size());
            } else {
                nonPersistedFiles = jsonFiles;
            }

            List<List<File>> fileLists = new ArrayList<>();

            // Persist a predefined number of block to the database at a time
            int persistenceThreshold = 2000;
            for (int i = 0; i < nonPersistedFiles.size(); i = i + persistenceThreshold) {
                int toIndex = i + persistenceThreshold;

                if (toIndex > nonPersistedFiles.size()) {
                    fileLists.add(nonPersistedFiles.subList(i, nonPersistedFiles.size()));
                } else {
                    fileLists.add(nonPersistedFiles.subList(i, toIndex));
                }
            }

            // Write each batch to the database
            int totalPersistCount = 0;
            for (List<File> fileList : fileLists) {

                // Get a new batch inserter for this job
                batchInserter = getBatchInserter();
                registerShutdownHook(batchInserter);

                int batchPersistCount = 0;
                int batchSize;
                for (File file : fileList) {
                    // Try for each block, to reduce any data loss.
                    try {
                        batchPersistCount++;
                        totalPersistCount++;
                        batchSize = fileList.size();

                        Block block = mapper.readValue(file, Block.class);

                        log.info("Persisting block " + block.getHash());
                        log.info("Count in current batch: " + batchPersistCount + "/" + batchSize);
                        log.info("Count in total: " + totalPersistCount + "/" + nonPersistedFiles.size());

                        for (Transaction transaction : block.getTx()) {
                            storeTransaction(transaction, batchInserter);
                        }

                    } catch (Exception e) {
                        log.error("Error writing to the database" , e);
                    }
                }

                // Store the results to the database
                writeBatch(batchInserter);
            }
        } catch (Exception e) {
            log.error("Error writing to database");
        } finally {
            // Always ensure inserter is shutdown
            if(!isShutdown && batchInserter != null) {
                writeBatch(batchInserter);
                GraphController.dbUpdated = true;
            }
        }
    }

    private void writeBatch(BatchInserter batchInserter) {
        log.info("Initiating graph shutdown.... this may take quite a while.");
        batchInserter.shutdown();
        isShutdown = true;
        log.info("Embedded database is now shutdown and ready to use in server mode");
    }

    private void storeTransaction(Transaction transaction, BatchInserter batchInserter) {
        long transactionNode;
        long walletNode;
        Map<String, Object> txProps = new HashMap<>();

        txProps.put(Props.INDEX, transaction.getTx_index());
        txProps.put(Props.HASH, transaction.getHash());
        txProps.put(Props.TIMESTAMP, transaction.getTime());

        transactionNode = batchInserter.createNode(txProps, Labels.TRANSACTION);

        if (transaction.getInputs().size() > 1) {
            walletNode = batchInserter.createNode(null, Labels.WALLET);
        } else {
            walletNode = 0;
        }

        // create node for each input operation and link it to the originating address
        for (Input input : transaction.getInputs()) {

            /**
             * lookup existing or create new address node
             */
            Long addressNode = addressIndex.get(input.getPrev_out().getAddr());
            if (addressNode == null) {
                Map<String, Object> addressProperties = new HashMap<>();
                addressProperties.put(Props.ADDR, input.getPrev_out().getAddr());

                addressNode = batchInserter.createNode(addressProperties, Labels.ADDRESS);

                addressIndex.put(input.getPrev_out().getAddr(), addressNode);
            }

            Map<String, Object> inProps = new HashMap<>();
            inProps.put(Props.AMOUNT, ((double) input.getPrev_out().getValue() * SATOSHI));
            inProps.put(Props.FROM_TX_INDEX, input.getPrev_out().getTx_index());

            // input withdraws from the address
            batchInserter.createRelationship(addressNode, transactionNode, Relationships.WITHDRAW, inProps);

            if (walletNode != 0) {
                batchInserter.createRelationship(walletNode, addressNode, Relationships.SAME_OWNER, null);
            }

        }

        // create output node for each operation and link to the destination address
        for (Output out : transaction.getOut())
        {
            Map<String, Object> outProps = new HashMap<>();

            outProps.put(Props.AMOUNT, ((double) out.getValue() * SATOSHI));
            outProps.put(Props.OUTPUT_NUM, out.getN());
            outProps.put(Props.TX_INDEX, transaction.getTx_index());

            Long addrNode = addressIndex.get(out.getAddr());
            if (addrNode == null) {
                Map<String, Object> addrProps = new HashMap<>();
                addrProps.put(Props.ADDR, out.getAddr());
                addrNode = batchInserter.createNode(addrProps, Labels.ADDRESS);

                addressIndex.put(out.getAddr(), addrNode);
            }

            // output deposits into the address
            batchInserter.createRelationship(transactionNode, addrNode, Relationships.DEPOSIT, outProps);

        }

    }

    private BatchInserter getBatchInserter() throws IOException {
        Map<String, String> config = new HashMap<String, String>() {{
            put("dump_configuration","false");
            put("cache_type","none");
            put("use_memory_mapped_buffers","false");
            put("neostore.nodestore.db.mapped_memory","1G");
            put("neostore.relationshipstore.db.mapped_memory","1G");
            put("neostore.propertystore.db.mapped_memory","1G");
            put("neostore.propertystore.db.strings.mapped_memory","1G");
        }};

        File dbPath = FileUtils.getFile(StringConstants.DB_PATH);

        BatchInserter inserter = BatchInserters.inserter(dbPath, config);
        isShutdown = false; // Inserter retrieved successfully, signal active
        return inserter;
    }

    /**
     * Registers a shutdown hook for a batch inserter. Emergency defense against corrupt
     * database if the JVM shutdowns unexpectedly without completing shutdown previously.
     *
     * @param batchInserter The batch inserter to shutdown.
     */
    private void registerShutdownHook(final BatchInserter batchInserter) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (!isShutdown) {
                    log.info("Initiating graph shutdown.... this may take quite a while.");
                    batchInserter.shutdown();
                    log.info("Graph is now shutdown and ready to use");
                }
            }
        } );
    }
}
