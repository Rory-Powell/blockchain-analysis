package rpowell.blockchain.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@JsonIdentityInfo(generator=JSOGGenerator.class)
@NodeEntity
public class PublicKey {

    @GraphId Long id;

    @Property(name="publicKey")
    private String key;

    @Relationship(type="SENT_TO", direction=Relationship.UNDIRECTED)
    private List<PublicKey> sentTo;

    public PublicKey() {}

    public PublicKey(String key) {
        this.key = key;
    }
}
