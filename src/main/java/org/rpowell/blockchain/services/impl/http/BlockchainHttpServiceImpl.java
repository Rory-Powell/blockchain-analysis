package org.rpowell.blockchain.services.impl.http;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.services.http.BaseHttpService;
import org.rpowell.blockchain.services.http.IBlockchainHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlockchainHttpServiceImpl extends BaseHttpService implements IBlockchainHttpService {

    private static Logger log = LoggerFactory.getLogger(IBlockchainHttpService.class);

    private final String RAW_BLOCK = "rawblock/";
    private final String RAW_ADDRESS = "rawaddr/";
    private final String LATEST_BLOCK = "latestblock";

    private final String ALT_URL = "https://blockexplorer.com/api/status?q=";
    private final String BLOCK_COUNT = "getBlockCount";

    public BlockchainHttpServiceImpl() {
        URI = "https://blockchain.info/";
    }

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

    /**
     * Get the current blockchain block count.
     * @return  The block count.
     */
    public int getCurrentBlockCount() {
        String requestURL = ALT_URL + BLOCK_COUNT;

        Map<String, Integer> result = new HashMap<>();
        result = restTemplate.getForObject(requestURL, result.getClass());

        return result.get("blockcount");
    }
}
