package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.PublicKeyRepository;

@Service
@Transactional
public class PublicKeyService {

    @Autowired
    PublicKeyRepository movieRepository;

}
