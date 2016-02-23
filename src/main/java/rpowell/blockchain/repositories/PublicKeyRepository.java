package rpowell.blockchain.repositories;

import rpowell.blockchain.domain.PublicKey;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "PublicKey", path = "PublicKey")
public interface PublicKeyRepository extends GraphRepository<PublicKey> {

    PublicKey findByPublicKey(@Param("key") String key);
}

