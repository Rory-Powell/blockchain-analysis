package parsing;

import org.bitcoinj.core.Block;
import org.bitcoinj.utils.BlockFileLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockChainParserTest {

    BlockChainParser blockChainParser;

    @Before
    public void setUp() throws Exception {
        blockChainParser = new BlockChainParser();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInitialisation() throws Exception {
        BlockFileLoader blockFileLoader = blockChainParser.blockFileLoader();
        assertTrue(blockFileLoader.hasNext());
    }

    @Test @Ignore
    public void testGetBlocks() throws Exception {
        List<Block> blocks = blockChainParser.getBlocks();
        int knownBlockCount = 119965;
        assertEquals(knownBlockCount, blocks.size());
    }
}