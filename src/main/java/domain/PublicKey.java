package domain;

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

    @Property(name="key")
    private String key;

    @Relationship(type="SENT", direction=Relationship.OUTGOING)
    private List<PublicKey> sentTo;

    @Relationship(type="SENT", direction=Relationship.INCOMING)
    private List<PublicKey> receivedFrom;

    public PublicKey() {}
}
