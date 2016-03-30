package org.rpowell.blockchain.services;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.util.Network;
import org.rpowell.blockchain.util.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.util.parsing.json.JSONObject;

import java.io.File;
import java.io.IOException;

@Service
public class FetcherServiceImpl implements IFetcherService {

    private static final Logger log = LoggerFactory.getLogger(FetcherServiceImpl.class);

    protected FetcherServiceImpl() {}

    private final String FILE_EXT = ".json";

    @Override
    public void writeBlockchainToJSON() {

        ObjectMapper mapper = new ObjectMapper();
        int count = 0;

        LatestBlock latestBlock = Network.getLatestBlock();
        Block previousBlock = Network.getBlockByhash(latestBlock.getHash());

        do {
            try {
                File newFile = new File(StringConstants.JSON_PATH + previousBlock.getBlock_index() + FILE_EXT);

                mapper.writeValue(newFile, previousBlock);
                log.info("Writing file " + newFile.toString() + " Count: " + count);

                previousBlock = Network.getBlockByhash(previousBlock.getPrev_block());

                count++;
            } catch (IOException e) {
               log.error("Error writing block to json", e);
            }

        } while (count < latestBlock.getBlock_index());

    }
}
