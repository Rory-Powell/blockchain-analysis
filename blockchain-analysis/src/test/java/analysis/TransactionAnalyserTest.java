package analysis;

import org.junit.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rpowell on 08/11/15.
 */
public class TransactionAnalyserTest {

    TransactionAnalyser transactionAnalyser = new TransactionAnalyser();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test @Ignore
    public void testGetBlocks() {
        List<DelegateBlock> blocks = transactionAnalyser.getBlocks();
        assertTrue(!blocks.isEmpty());
    }

    @Test @Ignore
    public void testAverageTransatransactionAnalyserctionCountPerBlock() {
        double averageTransactionCountPerBlock = transactionAnalyser.averageTransactionCountPerBlock();
        assertTrue(averageTransactionCountPerBlock > 50);
    }

    @Test @Ignore
    public void testAverageTransactionAmountPerBlock() {
//        double knownValue = 3.626782811653399;
//        double averageTransactionCountPerBlock = transactionAnalyser.averageTransactionAmountPerBlock();
//        assertEquals(knownValue, averageTransactionCountPerBlock, 0);
    }
}