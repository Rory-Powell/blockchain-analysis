package rpowell.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import rpowell.blockchain.domain.PublicKey;
import rpowell.blockchain.services.ParseService;
import rpowell.blockchain.services.PublicKeyService;
import java.io.IOException;

@Import(AppConfiguration.class)
public class App implements CommandLineRunner {

    @Autowired
    PublicKeyService pubKeyService;

    @Autowired
    ParseService parseService;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("Started Application");

        PublicKey publicKey = new PublicKey("Test-key-1");
        PublicKey publicKey2 = new PublicKey("Test-key-2");

//        pubKeyService.saveKey(publicKey);
//        pubKeyService.saveKey(publicKey2);

        PublicKey publicKey1 = pubKeyService.findByKey("Test-key-1");
        pubKeyService.deleteAll();
        PublicKey publicKey3 = pubKeyService.findByKey("Test-key-1");
    }
}