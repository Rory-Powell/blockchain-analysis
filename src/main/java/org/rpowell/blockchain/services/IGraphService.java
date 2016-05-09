package org.rpowell.blockchain.services;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Owner;

import java.util.List;

public interface IGraphService {

    List<Address> getAllAddresses();

    List<Address> getAssociatedAddresses(String address);

    int getAddressCount();

    int getOwnerCount();

    int getNodeCount();

    int getTransactionCount();

    void updateDatabase();

    void populateDatabase(int blockCount);

    int getCurrentBlockCount();

    List<Owner> getAllOwners();
}
