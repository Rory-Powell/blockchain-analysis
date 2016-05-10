package org.rpowell.blockchain.services.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.*;
import org.rpowell.blockchain.domain.Block;
import org.rpowell.blockchain.domain.LatestBlock;
import org.rpowell.blockchain.services.http.IBlockchainHttpService;
import org.rpowell.blockchain.util.common.StringConstants;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Fetcher service tests
 */
public class FetcherServiceImplTest {

    @Mock
    private IBlockchainHttpService blockchainHttpService;

    @Spy
    @InjectMocks
    private FetcherServiceImpl fetcherService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore
    public void writeBlockchainToJSON() throws Exception {
        // Given
        Block startBlock = new Block();
        startBlock.setBlock_index(1);

        Block genesisBlock = new Block();
        genesisBlock.setHash(StringConstants.GENESIS_BLOCK_HASH);

        LatestBlock latestBlock = new LatestBlock();
        latestBlock.setBlock_index(1);
        latestBlock.setHash("test");

        Mockito.when(blockchainHttpService.getLatestBlock()).thenReturn(latestBlock);
        Mockito.when(blockchainHttpService.getBlockByHash("test")).thenReturn(startBlock);
        Mockito.when(blockchainHttpService.getBlockByHash(genesisBlock.getHash())).thenReturn(genesisBlock);

        // When
        fetcherService.writeBlockchainToJSON();

        // Then
        verify(fetcherService, times(1)).downloadBlocks(any(), anyLong(), anyInt());
    }
}