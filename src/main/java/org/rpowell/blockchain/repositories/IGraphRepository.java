package org.rpowell.blockchain.repositories;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Owner;

import java.util.List;

public interface IGraphRepository {

    List<Address> getAllAddresses();

    List<Address> getAssociatedAddresses(String address);

    int getAddressCount();

    int getOwnerCount();

    int getNodeCount();

    int getTransactionCount();

    List<Owner> getAllOwners();

}
