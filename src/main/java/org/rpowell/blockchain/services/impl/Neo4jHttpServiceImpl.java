package org.rpowell.blockchain.services.impl;

import org.rpowell.blockchain.services.BaseHttpService;
import org.rpowell.blockchain.services.INeo4jHttpService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class Neo4jHttpServiceImpl extends BaseHttpService implements INeo4jHttpService {

    private HttpHeaders headers = new HttpHeaders();

    public Neo4jHttpServiceImpl() {
        URI = "http://localhost:7474/db/data/transaction/commit";
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic bmVvNGo6YmxvY2tjaGFpbg==");
    }

    /**
     * Query the database server.
     * @param query         The cypher query to run.
     * @param responseType  The response type to map to.
     * @return              The response entity.
     */
    public <T> ResponseEntity<T> queryDatabase(String query, Class<T> responseType) {
        String body = "{\"statements\" : [ {\"statement\" : \"" + query + "\"} ]}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(URI, HttpMethod.POST, entity, responseType);
    }
}
