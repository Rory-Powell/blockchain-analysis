package org.rpowell.blockchain.spring.repositories;

import org.rpowell.blockchain.domain.Address;

import java.util.List;

public interface IGraphRepository {

    List<Address> getAllAddresses();

}
