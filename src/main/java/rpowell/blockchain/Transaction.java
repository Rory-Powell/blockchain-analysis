package rpowell.blockchain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transaction {

    // The sender address
    private List<String> inputs = new ArrayList<>();

    // The receiver address
    private List<String> outputs = new ArrayList<>();

    public Transaction() {
    }

    public List<String> getOutputs() {
        return this.outputs;
    }

    public List<String> getInputs() {
        return this.inputs;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }

    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null) return false;
        return !(outputs != null ? !outputs.equals(that.outputs) : that.outputs != null);
    }

    @Override
    public int hashCode() {
        int result = inputs != null ? inputs.hashCode() : 0;
        result = 31 * result + (outputs != null ? outputs.hashCode() : 0);
        return result;
    }
}
