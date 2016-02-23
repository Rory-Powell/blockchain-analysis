package rpowell.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import rpowell.blockchain.services.ParseService;
import rpowell.blockchain.services.PublicKeyService;

import java.io.IOException;

@Configuration
@Import(DbConfiguration.class)
@RestController("/")
public class App extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Autowired
    PublicKeyService pubKeyService;

    @Autowired
    ParseService parseService;
}