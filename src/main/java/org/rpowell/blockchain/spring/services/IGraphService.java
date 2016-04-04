package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Address;

import java.util.List;

public interface IGraphService {

    List<Address> getAllAddresses();

}
