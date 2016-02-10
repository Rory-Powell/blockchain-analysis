package analysis;

import data.DelegateBlock;
import data.DelegateTransaction;
import filesystem.FileWalker;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.BlockParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockAnalyser {

    private static final Logger log = LoggerFactory.getLogger(BlockAnalyser.class);
    private static Wallet wallet = new Wallet(new MainNetParams());
    private List<DelegateBlock> blocks = null;
    private List<DelegateTransaction> allTransactions = null;
    private NetworkParameters netParams = new MainNetParams();
    private BlockParser blockParser;

    // Constructor
    public BlockAnalyser() {
        this.blockParser = new BlockParser(netParams);
    }

    public void detectCommunities() {
        blockParser.detectCommunities(FileWalker.discoverFilesOnDefaultPath());
    }
}
