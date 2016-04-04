package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Address;

import java.util.List;
import java.util.Map;

public interface IGraphService {

    List<Address> getAllAddresses();

    Map<String, Object> graph(String address, int depth);

}
