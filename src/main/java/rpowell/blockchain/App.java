package rpowell.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import rpowell.blockchain.domain.PublicKey;
import rpowell.blockchain.services.IParseService;
import rpowell.blockchain.services.IPublicKeyService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Import(AppConfiguration.class)
public class App implements CommandLineRunner {

    @Autowired
    IPublicKeyService pubKeyService;

    @Autowired
    IParseService parseService;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("Started Application");

        // Parse block files and stream write them to the DB
        pubKeyService.deleteAll();
        parseService.parseBlockFiles();
    }
}