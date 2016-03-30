package org.rpowell.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Input {

    PrevOut prev_out;
    long sequence;
    String script;

    public PrevOut getPrev_out() {
        return prev_out;
    }

    public void setPrev_out(PrevOut prev_out) {
        this.prev_out = prev_out;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long time) {
        this.sequence = time;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }


}
