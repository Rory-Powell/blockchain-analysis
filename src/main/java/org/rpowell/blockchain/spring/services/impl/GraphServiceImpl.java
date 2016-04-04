package org.rpowell.blockchain.spring.services.impl;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.spring.repositories.IGraphRepository;
import org.rpowell.blockchain.spring.services.IGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.rpowell.blockchain.graph.CypherQueries.*;

@Service
public class GraphServiceImpl implements IGraphService {

    @Autowired
    private IGraphRepository graphRepository;

    protected GraphServiceImpl() {}

    public List<Address> getAllAddresses() {
       return graphRepository.getAllAddresses();
    }

    public Map<String, Object> graph(String address, int depth) {
        return graphRepository.graph(getAddressQuery(), getAddressQueryParams(address, depth));
    }
}
