package rpowell.blockchain.repositories;

import org.springframework.stereotype.Repository;
import rpowell.blockchain.domain.PublicKey;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface IPublicKeyRepository extends GraphRepository<PublicKey> {

    PublicKey findByPublicKey(@Param("publicKey") String publicKey);
}

