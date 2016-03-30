package org.rpowell.blockchain.services;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.rpowell.blockchain.util.FileUtil;
import org.rpowell.blockchain.util.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParseServiceImpl implements IParseService {

    private static final Logger log = LoggerFactory.getLogger(IParseService.class);

    private GraphDatabaseService graphDb;
    private BatchInserter batchInserter;

    protected ParseServiceImpl() {

        try {
            batchInserter = getBatchInserter();
        } catch (IOException e) {
           log.error("Error getting batch inserter", e);
        }

        registerShutdownHook(batchInserter);
    }

    @Override
    public void writeJSONToDB() {
        try {
            List<File> jsonFiles = FileUtil.getFolderContents(StringConstants.JSON_PATH);

        } catch (Exception e) {

        } finally {
            log.info("Initiating graph shutdown.... this may take quite a while.");
            batchInserter.shutdown();
            log.info("Graph is now shutdown and ready to use");
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

        return BatchInserters.inserter(dbPath, config);
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
    private void registerShutdownHook(final GraphDatabaseService graphDb)
    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }

    /**
     * Registers a shutdown hook.
     * @param batchInserter The batch inserter to shutdown.
     */
    private void registerShutdownHook(final BatchInserter batchInserter)
    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                batchInserter.shutdown();
            }
        } );
    }
}
