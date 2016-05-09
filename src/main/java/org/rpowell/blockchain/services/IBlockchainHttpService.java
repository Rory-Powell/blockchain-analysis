package org.rpowell.blockchain.services;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;

public interface IBlockchainHttpService {

    Block getBlockByHash(String hash);

    LatestBlock getLatestBlock();

    Address getAddress(String address);

    int getCurrentBlockCount();

}
