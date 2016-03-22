package rpowell.blockchain.services;

import rpowell.blockchain.domain.PublicKey;

public interface IPublicKeyService {

    PublicKey findByKey(String key);

    void saveKey(PublicKey key);

    void saveAllKeys(Iterable<PublicKey> publicKeys);

    boolean checkExists(String key);

    void deleteAll();
}