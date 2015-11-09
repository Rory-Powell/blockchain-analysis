package parsing;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockChainParser {

    // The path to the block dataset
    String path = "/home/rpowell/dev/resources/blk00000.dat";

    // The file loader
    BlockFileLoader blockFileLoader;

    /**
     * Constructor
     */
    public BlockChainParser() {
        initialise();
    }

    /**
     * Constructor.
     * @param path The path to look for blocks in.
     */
    public BlockChainParser(String path) {
        this.path = path;
        initialise();
    }

    /**
     * Initialisation. Create the block file loader from the specified file path.
     */
    public void initialise() {
        NetworkParameters networkParameters = new MainNetParams();
        Context context = new Context(networkParameters);
        List<File> blockChainFiles = new ArrayList<>();
        blockChainFiles.add(new File(path));
        blockFileLoader = new BlockFileLoader(networkParameters, blockChainFiles);
    }

    /**
     * Retrieve a list of blocks from the block file loader.
     * @return
     */
    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();

        // Iterate over the blocks in the dataset.
        for (Block block : blockFileLoader) {
            blocks.add(block);
        }

        return blocks;
    }

    public BlockFileLoader blockFileLoader() {
        return blockFileLoader;
    }
}
