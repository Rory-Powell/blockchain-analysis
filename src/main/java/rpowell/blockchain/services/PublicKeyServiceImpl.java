package rpowell.blockchain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpowell.blockchain.domain.PublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpowell.blockchain.repositories.IPublicKeyRepository;

@Service
@Transactional
public class PublicKeyServiceImpl implements IPublicKeyService {

    @Autowired
    IPublicKeyRepository publicKeyRepository;

    private static final Logger log = LoggerFactory.getLogger(PublicKeyServiceImpl.class);

    public PublicKey findByKey(String key) {
        return publicKeyRepository.findByPublicKey(key);
    }

    public void saveKey(PublicKey key) {
        log.info("Saving public key " + key.getPublicKey());
        publicKeyRepository.save(key);
        log.info("Public key saved");
    }

    public void saveAllKeys(Iterable<PublicKey> publicKeys) {
        log.info("Saving batch of public keys");
        publicKeyRepository.save(publicKeys);
        log.info("Batch save complete");
    }

    public boolean checkExists(String key) {
        return publicKeyRepository.findByPublicKey(key) != null;
    }

    public void deleteAll() {
        log.info("Deleting all public keys");
        publicKeyRepository.deleteAll();
        log.info("All Public keys deleted");
    }
}
