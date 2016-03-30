package org.rpowell.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

    String hash;
    Integer ver;
    String prev_block;
    String mrkl_root;
    long time;
    long bits;
    long nonce;
    Integer n_tx;
    Integer size;
    long block_index;
    boolean main_chain;
    long height;
    long received_time;
    String relayed_by;
    List<Transaction> tx;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    public String getPrev_block() {
        return prev_block;
    }

    public void setPrev_block(String prev_block) {
        this.prev_block = prev_block;
    }

    public String getMrkl_root() {
        return mrkl_root;
    }

    public void setMrkl_root(String mrkl_root) {
        this.mrkl_root = mrkl_root;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBits() {
        return bits;
    }

    public void setBits(long bits) {
        this.bits = bits;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public Integer getN_tx() {
        return n_tx;
    }

    public void setN_tx(Integer n_tx) {
        this.n_tx = n_tx;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public long getBlock_index() {
        return block_index;
    }

    public void setBlock_index(long block_index) {
        this.block_index = block_index;
    }

    public boolean isMain_chain() {
        return main_chain;
    }

    public void setMain_chain(boolean main_chain) {
        this.main_chain = main_chain;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getReceived_time() {
        return received_time;
    }

    public void setReceived_time(long received_time) {
        this.received_time = received_time;
    }

    public String getRelayed_by() {
        return relayed_by;
    }

    public void setRelayed_by(String relayed_by) {
        this.relayed_by = relayed_by;
    }

    public List<Transaction> getTx() {
        return tx;
    }

    public void setTx(List<Transaction> tx) {
        this.tx = tx;
    }
}
