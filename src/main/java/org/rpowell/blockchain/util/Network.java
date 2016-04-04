package org.rpowell.blockchain.util;

import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class Network {

    private static Logger log = LoggerFactory.getLogger(Network.class);

    private static final String URL = "https://blockchain.info/";

    private static final String RAW_TRANSACTION = "rawtx/";
    private static final String RAW_BLOCK = "rawblock/";
    private static final String RAW_ADDRESS = "rawaddr/";

    private static final String LATEST_BLOCK = "latestblock";

    private static RestTemplate restTemplate = new RestTemplate();

    public static Block getBlockByhash(String hash) {
        log.info("Downloading block " + hash);
        return restTemplate.getForObject(URL + RAW_BLOCK + hash, Block.class);
    }

    public static LatestBlock getLatestBlock() {
        log.info("Downloading latest information");
        return restTemplate.getForObject(URL + LATEST_BLOCK, LatestBlock.class);
    }

    public static Transaction getTransactionByIndexOrHash(String indexOrHash) {
        return restTemplate.getForObject(URL + RAW_TRANSACTION + indexOrHash, Transaction.class);
    }

    public static Block getBlockByIndex(long index) {
        return restTemplate.getForObject(URL + RAW_BLOCK + index, Block.class);
    }

    public String getBlockByHash(String hash) {
        return restTemplate.getForObject(URL + RAW_BLOCK + hash, String.class);
    }

    public static Address getAddress(String address) {
        return restTemplate.getForObject(URL + RAW_ADDRESS + address, Address.class);
    }
}
