package org.rpowell.blockchain.spring.repositories.impl;

import org.rpowell.blockchain.domain.*;
import org.rpowell.blockchain.network.AddressesResponse;
import org.rpowell.blockchain.network.GraphRequests;
import org.rpowell.blockchain.spring.repositories.IGraphRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Repository
public class GraphRepositoryImpl implements IGraphRepository {

    private static final Logger log = LoggerFactory.getLogger(GraphRepositoryImpl.class);

    private String SERVER_ROOT_URI = "http://localhost:7474/db/data/transaction/commit";
    private RestTemplate restTemplate = new RestTemplate();

    protected GraphRepositoryImpl() {

    }

    /**
     * Get all the stored nodes with an address label.
     * @return  A resource iterator of address nodes.
     */
    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<>();

        // TODO : Move this logic
        String body = "{\"statements\" : [ {\"statement\" : \"" + "MATCH (n:Address) RETURN n.Addr LIMIT 1000" + "\"} ]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic bmVvNGo6YmxvY2tjaGFpbg==");

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AddressesResponse> response = restTemplate.exchange(SERVER_ROOT_URI, HttpMethod.POST, entity, AddressesResponse.class);

        AddressesResponse responseBody = response.getBody();

        ArrayList results = (ArrayList) responseBody.getResults();
        Map result = (Map) results.get(0);
        List<Map> data = (List<Map>) result.get("data");

        for (Map map : data) {
            Address address = new Address();
            List<String> values = (List<String>) map.get("row");
            address.setAddress(values.get(0));
            addresses.add(address);
        }

        return addresses;
    }
}
