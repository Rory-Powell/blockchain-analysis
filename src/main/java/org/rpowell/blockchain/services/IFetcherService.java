package org.rpowell.blockchain.services;

import org.rpowell.blockchain.domain.Block;

/**
 * Used to fetch new blocks from the internet and write them to
 * disk as JSON files.
 */
public interface IFetcherService {

    void writeBlockchainToJSON();

    void writeBlockchainToJSON(String JsonPath);

    void downloadBlocks(Block startBlock, long stopIndex);

}
