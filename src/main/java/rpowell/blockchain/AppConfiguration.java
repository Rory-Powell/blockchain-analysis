package rpowell.blockchain;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "rpowell.blockchain.repositories")
@EnableTransactionManagement
public class AppConfiguration extends Neo4jConfiguration {

    public static final String URL = "http://localhost:7474";

    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer(URL,"neo4j","blockchain");
    }

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory("rpowell.blockchain.domain");
    }
}