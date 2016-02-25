package rpowell.blockchain.analysis;

import org.junit.*;
import rpowell.blockchain.util.FileWalker;

import static org.junit.Assert.assertTrue;

public class FileWalkerTest {

    @Test
    public void testDiscoverFilesOnDefaultPath(){
        assertTrue(FileWalker.discoverFilesOnDefaultPath().size() > 0);
    }
}