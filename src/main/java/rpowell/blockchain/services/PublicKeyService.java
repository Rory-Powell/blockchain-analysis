package rpowell.blockchain.services;

import rpowell.blockchain.domain.PublicKey;

public interface PublicKeyService {

    PublicKey findByKey(String key);

    void saveKey(PublicKey key);

    void saveAllKeys(Iterable<PublicKey> publicKeys);
}
