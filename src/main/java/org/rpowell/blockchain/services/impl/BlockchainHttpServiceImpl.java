package org.rpowell.blockchain.services.impl;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.services.BaseHttpService;
import org.rpowell.blockchain.services.IHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockchainHttpServiceImpl extends BaseHttpService implements IHttpService {

    private static Logger log = LoggerFactory.getLogger(BlockchainHttpServiceImpl.class);

    private final String RAW_BLOCK = "rawblock/";
    private final String RAW_ADDRESS = "rawaddr/";
    private final String LATEST_BLOCK = "latestblock";

    private final String URI = "https://blockchain.info/";

    /**
     * Get a block given it's hash.
     * @param hash  The has of the block to get.
     * @return      The block.
     */
    public Block getBlockByHash(String hash) {
        log.info("Downloading block " + hash);
        return restTemplate.getForObject(URI + RAW_BLOCK + hash, Block.class);
    }

    /**
     * Get the latest block from the blockchain.
     * @return  The block.
     */
    public LatestBlock getLatestBlock() {
        log.info("Downloading latest information");
        return restTemplate.getForObject(URI + LATEST_BLOCK, LatestBlock.class);
    }

    /**
     * Get an address given it's identifier.
     * @param address   The identifier of the address.
     * @return          The address.
     */
    public Address getAddress(String address) {
        return restTemplate.getForObject(URI + RAW_ADDRESS + address, Address.class);
    }
}
