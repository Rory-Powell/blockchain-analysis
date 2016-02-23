package rpowell.blockchain.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class PublicKey extends DomainObject {

    @Property(name="publicKey")
    private String key;

    @Relationship(type="SENT_TO", direction=Relationship.UNDIRECTED)
    private List<PublicKey> sentTo;

    public PublicKey() {}

    public PublicKey(String key) {
        this.key = key;
    }
}
