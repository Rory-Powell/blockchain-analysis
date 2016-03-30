package org.rpowell.blockchain.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LatestBlock {

    String hash;
    long time;
    long block_index;
    List<String> txIndexes;

    public List<String> getTxIndexes() {
        return txIndexes;
    }

    public void setTxIndexes(List<String> txIndexes) {
        this.txIndexes = txIndexes;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBlock_index() {
        return block_index;
    }

    public void setBlock_index(long block_index) {
        this.block_index = block_index;
    }
}
