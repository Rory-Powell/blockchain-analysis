package org.rpowell.blockchain.network;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class Network {

    private static Logger log = LoggerFactory.getLogger(Network.class);

    private static final String URI = "https://blockchain.info/";

    private static final String RAW_TRANSACTION = "rawtx/";
    private static final String RAW_BLOCK = "rawblock/";
    private static final String RAW_ADDRESS = "rawaddr/";

    private static final String LATEST_BLOCK = "latestblock";

    private static RestTemplate restTemplate = new RestTemplate();

    public static Block getBlockByhash(String hash) {
        log.info("Downloading block " + hash);
        return restTemplate.getForObject(URI + RAW_BLOCK + hash, Block.class);
    }

    public static LatestBlock getLatestBlock() {
        log.info("Downloading latest information");
        return restTemplate.getForObject(URI + LATEST_BLOCK, LatestBlock.class);
    }

    public static Address getAddress(String address) {
        return restTemplate.getForObject(URI + RAW_ADDRESS + address, Address.class);
    }
}