package org.rpowell.blockchain.spring.repositories;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.rpowell.blockchain.domain.Address;

import java.util.List;

public interface IGraphRepository {

    List<Address> getAllAddresses();

    Node getAddress(String hash);

    Result execute(String query);
}
