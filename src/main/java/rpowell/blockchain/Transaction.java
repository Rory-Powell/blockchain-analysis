package rpowell.blockchain;

import java.util.HashSet;
import java.util.Set;

public class Transaction {

    // The sender address
    private Set<String> inputs = new HashSet<>();

    // The receiver address
    private Set<String> outputs = new HashSet<>();

    public Transaction() {
    }

    public void addInput(String input) {
        this.inputs.add(input);
    }

    public void addOutput(String output) {
        this.outputs.add(output);
    }

    public Set<String> getOutputs() {
        return this.outputs;
    }

    public Set<String> getInputs() {
        return this.inputs;
    }

    public void setOutputs(Set<String> outputs) {
        this.outputs = outputs;
    }

    public void setInputs(Set<String> inputs) {
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
