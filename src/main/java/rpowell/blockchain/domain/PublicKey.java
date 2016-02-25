package rpowell.blockchain.domain;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

public class PublicKey extends DomainObject {

    @Property
    private String publicKey;

    @Relationship(type="INPUT", direction=Relationship.UNDIRECTED)
    private Set<PublicKey> inputs = new HashSet<>();

    @Relationship(type="OUTPUT", direction=Relationship.UNDIRECTED)
    private Set<PublicKey> outputs = new HashSet<>();

    public PublicKey() {}

    public PublicKey(String key) {
        this.publicKey = key;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void linkInput(PublicKey input) {
        input.addOutput(this);
        this.inputs.add(input);
    }

    public void linkOutput(PublicKey output) {
        output.addInput(this);
        this.outputs.add(output);
    }

    public void addOutputs(Set<PublicKey> outputs) {
        this.outputs.addAll(outputs);
    }

    public void addInputs(Set<PublicKey> inputs) {
        this.inputs.addAll(inputs);
    }

    private void addInput(PublicKey input) {
        this.inputs.add(input);
    }

    private void addOutput(PublicKey output) {
        this.outputs.add(output);
    }
}
