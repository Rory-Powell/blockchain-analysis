package org.rpowell.blockchain.repositories.impl;

import org.rpowell.blockchain.domain.*;
import org.rpowell.blockchain.services.INeo4jHttpService;
import org.rpowell.blockchain.util.graph.GraphQueries;
import org.rpowell.blockchain.util.graph.GraphQueryResponse;
import org.rpowell.blockchain.repositories.IGraphRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class GraphRepositoryImpl implements IGraphRepository {

    private static final Logger log = LoggerFactory.getLogger(GraphRepositoryImpl.class);

    @Autowired
    private INeo4jHttpService neo4jHttpService;

    protected GraphRepositoryImpl() {}

    public int getAddressCount() {
        ResponseEntity<GraphQueryResponse> response = neo4jHttpService
                .queryDatabase(GraphQueries.addressCountQuery(), GraphQueryResponse.class);

        return extractCount(response);
    }

    public int getTransactionCount() {
        ResponseEntity<GraphQueryResponse> response = neo4jHttpService
                .queryDatabase(GraphQueries.transactionCountQuery(), GraphQueryResponse.class);

        return extractCount(response);
    }

    public int getOwnerCount() {
        ResponseEntity<GraphQueryResponse> response = neo4jHttpService
                .queryDatabase(GraphQueries.ownerCountQuery(), GraphQueryResponse.class);

        return extractCount(response);
    }

    public int getNodeCount() {
        ResponseEntity<GraphQueryResponse> response = neo4jHttpService
                .queryDatabase(GraphQueries.nodeCountQuery(), GraphQueryResponse.class);

        return extractCount(response);
    }

    public List<Address> getAssociatedAddresses(String address) {
        // Get the wallets associated with this address
        ResponseEntity<GraphQueryResponse> response = neo4jHttpService
                .queryDatabase(GraphQueries.ownerQuery(address), GraphQueryResponse.class);

        // Use set for unique answers
        Set<Address> addressSet = new HashSet<>();

        // For each wallet
        for (Map map : getDataMaps(response)) {
            List<Integer> values = (List<Integer>) map.get("row");
            int walletId = values.get(0);

            // Get all the addresses in that wallet
            ResponseEntity<GraphQueryResponse> response1 = neo4jHttpService
                    .queryDatabase(GraphQueries.addressesOfOwnerQuery(walletId), GraphQueryResponse.class);

            // Add them to the set
            for (Map map1 : getDataMaps(response1)) {
                Address address1 = new Address();
                List<String> values1 = (List<String>) map1.get("row");
                address1.setAddress(values1.get(0));
                addressSet.add(address1);
            }
        }

        // Remove the original from the results
        Address original = new Address();
        original.setAddress(address);
        addressSet.remove(original);

        return new ArrayList<>(addressSet);
    }

    /**
     * Get all the stored nodes with an address label.
     * @return  A resource iterator of address nodes.
     */
    public List<Address> getAllAddresses() {
        ResponseEntity<GraphQueryResponse> response = neo4jHttpService
                .queryDatabase(GraphQueries.addressesQuery(1000), GraphQueryResponse.class);

        List<Address> addresses = new ArrayList<>();

        for (Map map : getDataMaps(response)) {
            Address address = new Address();
            List<String> values = (List<String>) map.get("row");
            address.setAddress(values.get(0));
            addresses.add(address);
        }

        return addresses;
    }

    private int extractCount(ResponseEntity<GraphQueryResponse> response) {
        List<Map> maps = getDataMaps(response);
        Map map = maps.get(0);

        List<Integer> results = (List<Integer>) map.get("row");
        return results.get(0);
    }

    private List<Map> getDataMaps(ResponseEntity<GraphQueryResponse> responseEntity) {
        GraphQueryResponse addressesResponse = responseEntity.getBody();

        ArrayList results = (ArrayList) addressesResponse.getResults();
        Map result = (Map) results.get(0);
        return (List<Map>) result.get("data");
    }
}
