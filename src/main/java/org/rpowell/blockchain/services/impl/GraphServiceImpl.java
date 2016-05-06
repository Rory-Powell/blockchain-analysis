package org.rpowell.blockchain.services.impl;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.repositories.IGraphRepository;
import org.rpowell.blockchain.services.IFetcherService;
import org.rpowell.blockchain.services.IGraphService;
import org.rpowell.blockchain.services.IParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class GraphServiceImpl implements IGraphService {

    @Autowired
    private IGraphRepository graphRepository;

    @Autowired
    private IParseService parseService;

    @Autowired
    private IFetcherService fetcherService;

    @Autowired
    private Environment env;

    protected GraphServiceImpl() {}

    public List<Address> getAllAddresses() {
       return graphRepository.getAllAddresses();
    }

    public List<Address> getAssociatedAddresses(String address) {
        return graphRepository.getAssociatedAddresses(address);
    }

    public int getAddressCount() {
        return graphRepository.getAddressCount();
    }

    public int getTransactionCount() {
        return graphRepository.getTransactionCount();
    }

    public int getOwnerCount() {
        return graphRepository.getOwnerCount();
    }

    public int getNodeCount() {
        return graphRepository.getNodeCount();
    }

    /**
     * Shutdown the remote neo4j server.
     */
    public void shutdownServer() {
        String command = "/home/rpowell/apps/neo4j-community-2.3.3/bin/neo4j stop";
        executeCommand(command);
    }

    /**
     * Start the remote neo4j server.
     */
    public void startServer() {
        String command = "/home/rpowell/apps/neo4j-community-2.3.3/bin/neo4j start";
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
            e.printStackTrace();
        }

        return output.toString();

    }
}
