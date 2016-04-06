package org.rpowell.blockchain.network;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
public class GraphRequests {

    private static String SERVER_ROOT_URI = "http://localhost:7474/db/data/transaction/commit";
    private static RestTemplate restTemplate = new RestTemplate();
    private static HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic bmVvNGo6YmxvY2tjaGFpbg==");
    }

    public static <T> ResponseEntity<T> queryForObject(String query, Class<T> responseType) {
        String body = "{\"statements\" : [ {\"statement\" : \"" + query + "\"} ]}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        Object response = restTemplate.exchange(SERVER_ROOT_URI, HttpMethod.GET, entity, String.class);

        return restTemplate.exchange(SERVER_ROOT_URI, HttpMethod.GET, entity, responseType);
    }
}
