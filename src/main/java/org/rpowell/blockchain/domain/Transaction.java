package org.rpowell.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Summary data of a bitcoin transaction
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    String hash;
    Integer ver;
    Integer vin_sz;
    Integer vout_sz;
    long lock_time;
    long time;
    Integer size;
    String relayed_by;
    Integer block_height;
    boolean double_spend;
    long tx_index;
    List<Input> inputs;
    List<Output> out;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDouble_spend() {
        return double_spend;
    }

    public void setDouble_spend(boolean double_spend) {
        this.double_spend = double_spend;
    }

    public void setBlock_height(Integer block_height) {
        this.block_height = block_height;
    }

    public Integer getBlock_height() {
        return block_height;
    }

    public Integer getVout_sz() {
        return vout_sz;
    }

    public void setVout_sz(Integer vout_sz) {
        this.vout_sz = vout_sz;
    }

    public List<Output> getOut() {
        return out;
    }

    public void setOut(List<Output> outputs) {
        this.out = outputs;
    }

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

    public Integer getVin_sz() {
        return vin_sz;
    }

    public void setVin_sz(Integer vin_sz) {
        this.vin_sz = vin_sz;
    }

    public long getLock_time() {
        return lock_time;
    }

    public void setLock_time(long lock_time) {
        this.lock_time = lock_time;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getRelayed_by() {
        return relayed_by;
    }

    public void setRelayed_by(String relayed_by) {
        this.relayed_by = relayed_by;
    }

    public long getTx_index() {
        return tx_index;
    }

    public void setTx_index(long tx_index) {
        this.tx_index = tx_index;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }
}