package org.rpowell.blockchain.services.impl.http;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.rpowell.blockchain.App;
import org.rpowell.blockchain.domain.Address;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.services.http.IBlockchainHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestOperations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
public class BlockchainHttpServiceImplTest {

    @Mock
    private RestOperations template;

    @Autowired
    @InjectMocks
    private IBlockchainHttpService httpService;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBlockByHash() throws Exception {
        // Given: Mocked HTTP response
        Mockito.when(template.getForObject(anyString(), any())).thenReturn(new Block());
        // When
        Block testBlock = httpService.getBlockByHash("test");
        // Then
        verify(template, times(1)).getForObject(anyString(), Mockito.<Class<?>> any());
        assertNotNull(testBlock);
    }

    @Test
    public void getLatestBlock() throws Exception {
        // Given: Mocked HTTP response
        Mockito.when(template.getForObject(anyString(), any())).thenReturn(new LatestBlock());
        // When
        LatestBlock testBlock = httpService.getLatestBlock();
        // Then
        verify(template, times(1)).getForObject(anyString(), Mockito.<Class<?>> any());
        assertNotNull(testBlock);
    }

    @Test
    public void getAddress() throws Exception {
        // Given: Mocked HTTP response
        Mockito.when(template.getForObject(anyString(), any())).thenReturn(new Address());
        // When
        Address testAddress = httpService.getAddress("test");
        // Then
        verify(template, times(1)).getForObject(anyString(), Mockito.<Class<?>> any());
        assertNotNull(testAddress);
    }
}