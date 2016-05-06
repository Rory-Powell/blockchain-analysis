package org.rpowell.blockchain.services;

import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public abstract class BaseHttpService {

    protected String URI;
    protected RestOperations restTemplate = new RestTemplate();

}
