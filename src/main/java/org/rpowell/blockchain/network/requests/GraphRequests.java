package org.rpowell.blockchain.network.requests;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 *  Rest requests against the local neo4j database.
 */
public class GraphRequests {

    private static final String URI = "http://localhost:7474/db/data/transaction/commit";
    private static RestTemplate restTemplate = new RestTemplate();
    private static HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic bmVvNGo6YmxvY2tjaGFpbg==");
    }

    public static <T> ResponseEntity<T> queryForObject(String query, Class<T> responseType) {
        String body = "{\"statements\" : [ {\"statement\" : \"" + query + "\"} ]}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(URI, HttpMethod.POST, entity, responseType);
    }
}
