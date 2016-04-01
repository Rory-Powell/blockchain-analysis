package org.rpowell.blockchain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.Input;
import org.rpowell.blockchain.domain.Output;
import org.rpowell.blockchain.domain.Transaction;
import org.rpowell.blockchain.graph.Labels;
import org.rpowell.blockchain.graph.Relationships;
import org.rpowell.blockchain.util.FileUtil;
import org.rpowell.blockchain.util.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ParseServiceImpl implements IParseService {

    private static final Logger log = LoggerFactory.getLogger(IParseService.class);
    private final double SATOSHI = 0.00000001;
    private final ObjectMapper mapper = new ObjectMapper();

    private GraphDatabaseService graphDb;
    private boolean isShutdown = false;

    private Map<String, Long> addressIndex = new HashMap<>();

    protected ParseServiceImpl() {}

    @Override
    public void writeJSONToDB() {
        BatchInserter batchInserter = null;
        try {
            List<File> jsonFiles = FileUtil.getFolderContents(StringConstants.JSON_PATH);
            Collections.sort(jsonFiles); // Sort the files from earliest to latest

            List<List<File>> fileLists = new ArrayList<>();

            // Persist a predefined number of block to the database at a time
            int persistenceThreshold = 2000;
            for (int i = 0; i < jsonFiles.size(); i = i + persistenceThreshold) {
                int toIndex = i + persistenceThreshold;

                if (toIndex > jsonFiles.size()) {
                    fileLists.add(jsonFiles.subList(i, jsonFiles.size() - 1));
                } else {
                    fileLists.add(jsonFiles.subList(i, toIndex));
                }
            }

            // Write each batch to the database
            int totalPersistCount = 0;
            for (List<File> fileList : fileLists) {

                // Get a new batch inserter for this job
                batchInserter = getBatchInserter();
                registerShutdownHook(batchInserter);

                int batchPersistCount = 0;
                for (File file : fileList) {
                    // Try for each block, to reduce any data loss.
                    try {
                        batchPersistCount++;
                        totalPersistCount++;

                        Block block = mapper.readValue(file, Block.class);

                        log.info("Persisting block " + block.getHash());
                        log.info("Count in current batch: " + batchPersistCount + "/" + persistenceThreshold);
                        log.info("Count in total: " + totalPersistCount + "/" + jsonFiles.size());

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
            if(!isShutdown) {
                writeBatch(batchInserter);
            }
        }
    }

    private void writeBatch(BatchInserter batchInserter) {
        log.info("Initiating graph shutdown.... this may take quite a while.");
        batchInserter.shutdown();
        isShutdown = true;
        log.info("Graph is now shutdown and ready to use");
    }

    private void storeTransaction(Transaction transaction, BatchInserter batchInserter)
    {
        long txNode;
        long walletNode;
        Map<String, Object> txProps = new HashMap<>();

        txProps.put("Index", transaction.getTx_index());
        txProps.put("Hash", transaction.getHash());
        txProps.put("Timestamp", transaction.getTime());

        txNode = batchInserter.createNode(txProps, Labels.TRANSACTION);

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
            Long addrNode = addressIndex.get(input.getPrev_out().getAddr());
            if (addrNode == null) {
                Map<String, Object> addressProperties = new HashMap<>();
                addressProperties.put("Addr", input.getPrev_out().getAddr());

                addrNode = batchInserter.createNode(addressProperties, Labels.ADDRESS);

                addressIndex.put(input.getPrev_out().getAddr(), addrNode);
            }


            Map<String, Object> inProps = new HashMap<>();
            inProps.put("Amount", ((double) input.getPrev_out().getValue() * SATOSHI));
            inProps.put("FromTxIndex", input.getPrev_out().getTx_index());

            // input withdraws from the address
            batchInserter.createRelationship(addrNode, txNode, Relationships.WITHDRAW, inProps);

            if (walletNode != 0) {
                batchInserter.createRelationship(walletNode, addrNode, Relationships.SAME_OWNER, null);
            }

        }

        // create output node for each operation and link to the destination address
        for (Output out : transaction.getOut())
        {
            Map<String, Object> outProps = new HashMap<>();

            outProps.put("Amount", ((double) out.getValue() * SATOSHI));
            outProps.put("OutputNum", out.getN());
            outProps.put("TxIndex", transaction.getTx_index());

            Long addrNode = addressIndex.get(out.getAddr());
            if (addrNode == null) {
                Map<String, Object> addrProps = new HashMap<>();
                addrProps.put("Addr", out.getAddr());
                addrNode = batchInserter.createNode(addrProps, Labels.ADDRESS);

                addressIndex.put(out.getAddr(), addrNode);
            }

            // output deposits into the address
            batchInserter.createRelationship(txNode, addrNode, Relationships.DEPOSIT, outProps);
            log.info("Persisting transaction " + transaction.getHash());

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

    private GraphDatabaseService getGraphDatabaseService() {
        File dbPath = FileUtils.getFile(StringConstants.DB_PATH);
        File configPath = FileUtils.getFile(StringConstants.DB_PATH);

        return new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbPath)
                                            .loadPropertiesFromFile(configPath.toString())
                                            .newGraphDatabase();
    }

    /**
     * Registers a shutdown hook;
     * @param graphDb The graph database service to shutdown.
     */
    private void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
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