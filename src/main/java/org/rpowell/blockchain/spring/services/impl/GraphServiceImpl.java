package org.rpowell.blockchain.spring.services.impl;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.spring.repositories.GraphRepository;
import org.rpowell.blockchain.spring.services.IGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GraphServiceImpl implements IGraphService {

    @Autowired
    private GraphRepository graphRepository;

    protected GraphServiceImpl() {}

    public List<Address> getAllAddresses() {
       return graphRepository.getAllAddresses();
    }
}
