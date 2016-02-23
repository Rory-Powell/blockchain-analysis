package rpowell.blockchain.domain;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

public class PublicKey extends DomainObject {

    @Property
    private String publicKey;

    @Relationship(type="SENT_TO", direction=Relationship.UNDIRECTED)
    private List<PublicKey> sentTo;

    public PublicKey() {}

    public PublicKey(String key) {
        this.publicKey = key;
    }
}
