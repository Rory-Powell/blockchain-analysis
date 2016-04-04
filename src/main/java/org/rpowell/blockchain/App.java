package org.rpowell.blockchain;

import org.rpowell.blockchain.spring.services.IFetcherService;
import org.rpowell.blockchain.spring.services.IGraphService;
import org.rpowell.blockchain.spring.services.IParseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class App implements CommandLineRunner {

	@Autowired
	private IParseService parseService;

	@Autowired
	private IFetcherService fetcherService;

	@Autowired
	private IGraphService graphService;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(App.class, args);
	}

	private static final Logger log = LoggerFactory.getLogger(App.class);

	@Override
	public void run(String... args) throws Exception {
		log.info("Started Application");

		graphService.getAllAddresses();

//        fetcherService.writeBlockchainToJSON();
//        parseService.writeJSONToDB();
	}
}
