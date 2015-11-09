package analysis;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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