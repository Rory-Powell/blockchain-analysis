package org.rpowell.blockchain.services.http;

import org.rpowell.blockchain.domain.Owner;

import java.util.List;

public interface IOwnerHttpService {

    List<Owner> getOwners();
}
