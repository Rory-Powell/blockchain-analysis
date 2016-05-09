package org.rpowell.blockchain.services.http;

import org.springframework.http.ResponseEntity;

public interface INeo4jHttpService {

    <T> ResponseEntity<T> queryDatabase(String query, Class<T> responseType);

}