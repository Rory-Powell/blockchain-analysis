package org.rpowell.blockchain.spring.repositories.impl;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.kernel.GraphDatabaseAPI;
//import org.neo4j.server.WrappingNeoServerBootstrapper;
//import org.neo4j.server.configuration.Configurator;
//import org.neo4j.server.configuration.ServerConfigurator;
import org.rpowell.blockchain.domain.*;
import org.rpowell.blockchain.graph.GraphConstants;
import org.rpowell.blockchain.spring.repositories.IGraphRepository;
import org.rpowell.blockchain.util.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.util.*;

import static org.rpowell.blockchain.graph.GraphConstants.*;

@Repository
public class GraphRepositoryImpl implements IGraphRepository {

    private static final Logger log = LoggerFactory.getLogger(GraphRepositoryImpl.class);

    private GraphDatabaseService graphDb;
//    private WrappingNeoServerBootstrapper serverBootstrapper;

    protected GraphRepositoryImpl() {
        File dbPath = FileUtils.getFile(StringConstants.DB_PATH);
        File configPath = FileUtils.getFile(StringConstants.DB_PATH);

//        // Get an embedded database
//        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbPath)
//                                            .loadPropertiesFromFile(configPath.toString())
//                                            .newGraphDatabase();
//
//        registerShutdownHook(graphDb);

        // Start the db in server mode - For graph queries
//        connectAndStartBootstrapper(graphDb); TODO - Investigate if this is still possible
    }


    public Map<String, Object> graph(String query) {
        return toD3Format(query);
    }

    /**
     * Execute a cypher query against the neo4j database.
     * @param query         The query to execute.
     * @return              The result.
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
            address = graphDb.findNode(Labels.ADDRESS, GraphConstants.Properties.ADDR, hash);
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
                    address.setAddress((String) addressNode.getProperty(GraphConstants.Properties.ADDR));

                    addresses.add(address);
                    count++;
                }
            }

            tx.success();
        }

        return addresses;
    }

    // TODO - Modify this code to work with blockchain
    private Map<String, Object> toD3Format(String query) {
        try(Transaction tx = graphDb.beginTx()) {

            List<Map<String,Object>> nodes = new ArrayList<>();
            List<Map<String,Object>> relationships = new ArrayList<>();

            try (Result result = execute(query)) {

                int i=0;
                while (result.hasNext()) {
                    Map<String, Object> row = result.next();
                    nodes.add(map("title", row.get("movie"), "label", "movie"));

                    int target=i;
                    i++;

                    for (Object name : (Collection) row.get("cast")) {
                        Map<String, Object> actor = map("title", name,"label","actor");
                        int source = nodes.indexOf(actor);

                        if (source == -1) {
                            nodes.add(actor);
                            source = i++;
                        }

                        relationships.add(map("source",source,"target",target));
                    }
                }
            }

            tx.success();
            return map("nodes", nodes, "links", relationships);
        }
    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<>(2);
        result.put(key1,value1);
        result.put(key2,value2);
        return result;
    }

//    private void connectAndStartBootstrapper(GraphDatabaseService graphDb) {
//
//        try {
//            GraphDatabaseAPI api = (GraphDatabaseAPI) graphDb;
//
//            ServerConfigurator config = new ServerConfigurator(api);
//            config.configuration().addProperty(Configurator.WEBSERVER_ADDRESS_PROPERTY_KEY, "127.0.0.1");
//            config.configuration().addProperty(Configurator.WEBSERVER_PORT_PROPERTY_KEY, "7575");
//
//            serverBootstrapper = new WrappingNeoServerBootstrapper(api, config);
//            serverBootstrapper.start();
//
//            registerShutdownHook(serverBootstrapper);
//
//        } catch(Exception e) {
//            //handle appropriately
//        }
//    }

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

//    private void registerShutdownHook(final WrappingNeoServerBootstrapper serverBootstrapper) {
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                serverBootstrapper.stop();
//            }
//        });
//    }
}
