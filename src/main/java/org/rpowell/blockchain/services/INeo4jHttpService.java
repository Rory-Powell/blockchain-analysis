package org.rpowell.blockchain.services;

import org.springframework.http.ResponseEntity;

public interface INeo4jHttpService {

    <T> ResponseEntity<T> queryDatabase(String query, Class<T> responseType);

}