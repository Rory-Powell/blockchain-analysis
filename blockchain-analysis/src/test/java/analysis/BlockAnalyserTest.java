package analysis;

import org.bitcoinj.core.Block;
import org.bitcoinj.utils.BlockFileLoader;
import org.junit.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockAnalyserTest {

    BlockAnalyser blockAnalyser = new BlockAnalyser();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test @Ignore
    public void testGetBlocks() {
        List<Block> blocks = blockAnalyser.getBlocks();
        int knownBlockCount = 119965;
        assertEquals(knownBlockCount, blocks.size());
    }

    @Test
    public void testAverageTransactionCountPerBlock() {
        double knownValue = 3.626782811653399;
        double averageTransactionCountPerBlock = blockAnalyser.averageTransactionCountPerBlock();
        assertEquals(knownValue, averageTransactionCountPerBlock, 0);
    }

    @Test @Ignore
    public void testAverageTransactionAmountPerBlock() {
        double knownValue = 3.626782811653399;
        double averageTransactionCountPerBlock = blockAnalyser.averageTransactionAmountPerBlock();
        assertEquals(knownValue, averageTransactionCountPerBlock, 0);
    }
}