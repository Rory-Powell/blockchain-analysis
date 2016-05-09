package org.rpowell.blockchain.services.impl;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.domain.Owner;
import org.rpowell.blockchain.repositories.IGraphRepository;
import org.rpowell.blockchain.services.http.IBlockchainHttpService;
import org.rpowell.blockchain.services.IFetcherService;
import org.rpowell.blockchain.services.IGraphService;
import org.rpowell.blockchain.services.IParseService;
import org.rpowell.blockchain.util.PropertyLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class GraphServiceImpl implements IGraphService {

    private static Logger log = LoggerFactory.getLogger(GraphServiceImpl.class);

    @Autowired
    private IGraphRepository graphRepository;

    @Autowired
    private IParseService parseService;

    @Autowired
    private IFetcherService fetcherService;

    @Autowired
    private IBlockchainHttpService blockchainHttpService;

    private String DB_PATH = PropertyLoader.loadProperty("database.path");

    protected GraphServiceImpl() {
        startDbServer();
        registerShutdownHook();
    }

    @Override
    public List<Address> getAllAddresses() {
       return graphRepository.getAllAddresses();
    }

    @Override
    public List<Owner> getAllOwners() {
        return graphRepository.getAllOwners();
    }

    @Override
    public List<Address> getAssociatedAddresses(String address) {
        return graphRepository.getAssociatedAddresses(address);
    }

    @Override
    public int getAddressCount() {
        return graphRepository.getAddressCount();
    }

    @Override
    public int getTransactionCount() {
        return graphRepository.getTransactionCount();
    }

    @Override
    public int getOwnerCount() {
        return graphRepository.getOwnerCount();
    }

    @Override
    public int getNodeCount() {
        return graphRepository.getNodeCount();
    }

    @Override
    public int getCurrentBlockCount() {
        LatestBlock latestBlock = blockchainHttpService.getLatestBlock();
        return (int) latestBlock.getBlock_index();
    }

    @Override
    public void updateDatabase() {
        shutdownDbServer();
        fetcherService.writeBlockchainToJSON();
        parseService.writeJSONToDB();
        startDbServer();
    }

    @Override
    public void populateDatabase(int blockCount) {
        shutdownDbServer();
        fetcherService.writeBlockchainToJSON(blockCount);
        parseService.writeJSONToDB();
        startDbServer();
    }

    /**
     * Shutdown the remote neo4j server.
     */
    private void shutdownDbServer() {
        log.info("Stopping the Neo4j database server");
        String command = DB_PATH + "bin/neo4j stop";
        executeCommand(command);
    }

    /**
     * Start the remote neo4j server.
     */
    private void startDbServer() {
        log.info("Starting the Neo4j database server");
        String command = DB_PATH + "bin/neo4j start";
        executeCommand(command);
    }

    /**
     * Execute a shell command.
     * @param command   The command to execute.
     * @return          The output.
     */
    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            log.error("Error executing command", e);
        }

        return output.toString();
    }

    /**
     * Registers a shutdown hook to stop the neo4j Database server.
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdownDbServer();
            }
        } );
    }
}
