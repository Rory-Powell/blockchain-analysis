package org.rpowell.blockchain.spring.services;

/**
 * Used to fetch new blocks from the internet and write them to
 * disk as JSON files.
 */
public interface IFetcherService {

    void writeBlockchainToJSON();

}
