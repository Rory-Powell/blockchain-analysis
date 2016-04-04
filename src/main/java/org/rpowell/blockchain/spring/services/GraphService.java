package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.spring.repositories.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GraphService implements IGraphService {

    @Autowired
    private GraphRepository graphRepository;

    protected GraphService() {}

    public List<Address> getAllAddresses() {
       return graphRepository.getAllAddresses();
    }
}
