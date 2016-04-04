package org.rpowell.blockchain.spring.repositories.impl;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.rpowell.blockchain.domain.*;
import org.rpowell.blockchain.spring.repositories.IGraphRepository;
import org.rpowell.blockchain.util.StringConstants;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.rpowell.blockchain.graph.GraphConstants.*;

@Repository
public class GraphRepositoryImpl implements IGraphRepository {

    private GraphDatabaseService graphDb;

    protected GraphRepositoryImpl() {
        File dbPath = FileUtils.getFile(StringConstants.DB_PATH);
        File configPath = FileUtils.getFile(StringConstants.DB_PATH);

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbPath)
                                            .loadPropertiesFromFile(configPath.toString())
                                            .newGraphDatabase();

        registerShutdownHook(graphDb);
    }

    /**
     * Execute a cypher query against the neo4j database.
     * @param query The query to execute.
     * @return      The result.
     */
    public Result execute(String query) {
        Result result;

        try (Transaction tx = graphDb.beginTx()) {
            result = graphDb.execute(query);
            tx.success();
        }

        return result;
    }

    /**
     * Get the stored node with an address label the supplied address hash.
     * @param hash  The hash of the address to find.
     * @return      An address node.
     */
    public Node getAddress(String hash) {
        Node address;

        try (Transaction tx = graphDb.beginTx()) {
            address = graphDb.findNode(Labels.ADDRESS, "Addr", hash);
            tx.success();
        }

        return  address;
    }

    /**
     * Get all the stored nodes with an address label.
     * @return  A resource iterator of address nodes.
     */
    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<>();

        try (Transaction tx = graphDb.beginTx()) {
            try(ResourceIterator resourceIterator =  graphDb.findNodes(Labels.ADDRESS)) {
                int limit = 100;
                int count = 0;
                while (resourceIterator.hasNext() && count < limit) {
                    Node addressNode = (Node) resourceIterator.next();

                    Address address = new Address();
                    address.setAddress((String) addressNode.getProperty("Addr"));

                    addresses.add(address);
                    count++;
                }
            }

            tx.success();
        }

        return addresses;
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
}