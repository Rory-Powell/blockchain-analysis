package org.rpowell.blockchain.services.impl.http;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.rpowell.blockchain.App;
import org.rpowell.blockchain.services.http.INeo4jHttpService;
import org.rpowell.blockchain.util.graph.GraphQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestOperations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
public class Neo4jHttpServiceImplTest {

    @Mock
    private RestOperations template;

    @Autowired
    @InjectMocks
    private INeo4jHttpService neo4jHttpService;

    @Before
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore
    public void queryDatabase() throws Exception {
        // When
        neo4jHttpService.queryDatabase("Test", GraphQueryResponse.class);

        // Then
        Mockito.verify(template, Mockito.times(1))
                .exchange(anyString(), any(HttpMethod.class), any(), Mockito.<Class<?>> any());
    }
}