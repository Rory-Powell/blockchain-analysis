package repositories;

import domain.PublicKey;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "pubKeys", path = "pubKeys")
public interface PublicKeyRepository extends GraphRepository<PublicKey> {

    PublicKey findByKey(@Param("key") String key);
}

