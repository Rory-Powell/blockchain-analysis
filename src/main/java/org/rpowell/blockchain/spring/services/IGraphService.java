package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Address;

import java.util.List;

public interface IGraphService {

    List<Address> getAllAddresses();

    List<Address> getAssociatedAddresses(String address);

    int getAddressCount();

    int getOwnerCount();

    int getNodeCount();

    int getTransactionCount();

    void shutdownServer();

    void startServer();

}
