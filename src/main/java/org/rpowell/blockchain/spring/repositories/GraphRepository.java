package org.rpowell.blockchain.spring.repositories;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.graph.Labels;
import org.rpowell.blockchain.util.StringConstants;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GraphRepository {

    private GraphDatabaseService graphDb;

    protected GraphRepository() {
        File dbPath = FileUtils.getFile(StringConstants.DB_PATH);
        File configPath = FileUtils.getFile(StringConstants.DB_PATH);

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbPath)
                                            .loadPropertiesFromFile(configPath.toString())
                                            .newGraphDatabase();

        registerShutdownHook(graphDb);
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

                int limit = 50;
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
