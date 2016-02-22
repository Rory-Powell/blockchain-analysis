package data;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class PublicKey {

    @GraphId Long id;
    public String name;

    public PublicKey() {}
    public PublicKey(String name) { this.name = name; }

    @RelatedTo(type="TEAMMATE", direction=Direction.BOTH)
    public @Fetch
    Set<PublicKey> teammates;

    public void worksWith(PublicKey publicKey) {
        if (teammates == null) {
            teammates = new HashSet<PublicKey>();
        }
        teammates.add(publicKey);
    }

    public String toString() {
        String results = name + "'s teammates include\n";
        if (teammates != null) {
            for (PublicKey publicKey : teammates) {
                results += "\t- " + publicKey.name + "\n";
            }
        }
        return results;
    }
}