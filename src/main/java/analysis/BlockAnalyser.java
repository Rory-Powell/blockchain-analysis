package analysis;

import filesystem.FileWalker;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.BlockParser;

public class BlockAnalyser {

    private static final Logger log = LoggerFactory.getLogger(BlockAnalyser.class);
    private static Wallet wallet = new Wallet(new MainNetParams());
    private NetworkParameters netParams = new MainNetParams();
    private BlockParser blockParser;

    // Constructor
    public BlockAnalyser() {
        this.blockParser = new BlockParser(netParams);
    }

    public void writeToDB() {
//        blockParser.parseBlockFiles(FileWalker.discoverFilesOnDefaultPath());
    }

}
