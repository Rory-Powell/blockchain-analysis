package parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.DelegateBlock;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockParser {

    private static final Logger log = LoggerFactory.getLogger(BlockParser.class);

    // Parameters for connecting to the bitcoin network
    private NetworkParameters networkParameters;
    // The configuration context, stashed in thread local storage (TLS)
    private Context context;

    private ObjectMapper mapper = new ObjectMapper();
    private int fileNameIncrement = 0;

    /**
     * Constructor.
     */
    public BlockParser() {
        networkParameters = new MainNetParams();
        context = new Context(networkParameters);
    }

    /**
     * Parse and write a list of files as json.
     * @param files The files to write.
     * @param path  The path to write files.
     */
    public void writeFilesAsBlocks(List<File> files, String path) {
        // Logging setup
        long startTimeMill = System.currentTimeMillis();
        BlockFileLoader blockFileLoader = new BlockFileLoader(networkParameters, files);

        // Iterate over the blocks in the file.
        for (Block block : blockFileLoader) {
            DelegateBlock delegateBlock = new DelegateBlock(block);

//            try {
                // Build file path
                String filePath = path.concat("/" + fileNameIncrement + ".json");
                fileNameIncrement++;

                // Write to disk
                File file = new File(filePath);
                log.info("Parsed block " + delegateBlock.getBlockHash() + ". Writing to disk as " + file.getName());
//                mapper.writeValue(file, delegateBlock);
//            } catch (IOException e) {
//                log.error("IOException while writing file", e);
//            }
        }

        logTiming(startTimeMill);
    }

    /**
     * Read files on a path as {@link DelegateBlock}'s
     * @param path  The path to read from
     * @return      The List of blocks.
     */
    public List<DelegateBlock> readFilesAsBlocks(String path) {
        long startTimeMill = System.currentTimeMillis();
        List<File> filesOnJsonPath = FileWalker.discoverFilesOnPath(path);
        List<DelegateBlock> blocks = new ArrayList<>();
        for (File file : filesOnJsonPath) {
            log.info("Reading file " + file);
            try {
                DelegateBlock block = mapper.readValue(file, DelegateBlock.class);
                blocks.add(block);
            } catch (IOException e) {
                log.error("IOException while reading file " + file, e);
            }
        }

        logTiming(startTimeMill);
        return blocks;
    }

    public static void logTiming(long startTimeMill) {
        long endTimeMill = System.currentTimeMillis();
        long parseTimeSec = TimeUnit.MILLISECONDS.toSeconds(endTimeMill - startTimeMill);
        log.info("Parse took " + parseTimeSec + " seconds");
    }
}
