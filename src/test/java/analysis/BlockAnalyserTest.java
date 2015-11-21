package analysis;

import data.DelegateBlock;
import org.junit.*;
import parsing.BlockParser;
import parsing.FileSystemHandler;
import parsing.FileWalker;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

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
    public void testParseAndWriteBlocks() {
        long start = System.currentTimeMillis();
        blockAnalyser.parseAndWriteBlocks();
//        List<File> files = FileWalker.discoverFilesOnPath(FileSystemHandler.JSON_PATH);
//        assertTrue(!files.isEmpty());
        BlockParser.logTiming(start);
    }

    @Test @Ignore
    public void testReadBlocksFromDisk() {
        long start = System.currentTimeMillis();
        blockAnalyser.readBlocksFromDisk();
        List<DelegateBlock> blocks = blockAnalyser.getBlocks();
        assertTrue(!blocks.isEmpty());
        BlockParser.logTiming(start);
    }

    @Test @Ignore
    public void testAverageTransactionCountPerBlock() {
        double averageTransactionCountPerBlock = blockAnalyser.averageTransactionCountPerBlock();
        assertTrue(averageTransactionCountPerBlock > 50);
    }
}