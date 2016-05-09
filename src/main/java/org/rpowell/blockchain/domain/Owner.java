package org.rpowell.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {

    private String bitcoinaddress;

    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getBitcoinaddress() {
        return bitcoinaddress;
    }

    public void setBitcoinaddress(String bitcoinaddress) {
        this.bitcoinaddress = bitcoinaddress;
    }
}
