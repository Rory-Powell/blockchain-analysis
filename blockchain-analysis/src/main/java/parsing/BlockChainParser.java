package parsing;

import analysis.DelegateBlock;
import org.apache.commons.io.FilenameUtils;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockChainParser {

    // Logger
    private static final Logger log = LoggerFactory.getLogger(BlockChainParser.class);

    // The file loader
    private BlockFileLoader blockFileLoader;

    // Parameters for connecting to the bitcoin network
    private NetworkParameters networkParameters;

    // The configuration context, stashed in thread local storage (TLS).
    private Context context;

    // The list of blocks parsed from the file path.
    List<DelegateBlock> blocks;

    /**
     * Constructor.
     * @param path The path to look for blocks in.
     */
    public BlockChainParser(String path) {
        networkParameters = new MainNetParams();
        context = new Context(networkParameters);
        initialise(path);
    }

    /**
     * Reset the file path for this parser.
     * The parser will then automatically search the path for block files.
     * @param path The new path to look for file on.
     */
    public void setNewFilePath(String path) {
        initialise(path);
    }

    /**
     * Get the currently found blocks on the file path.
     * @return The list of blocks.
     */
    public List<DelegateBlock> getBlocks () {
        if (blocks == null) {
            blocks = parseBlocks();
        }
        return blocks;
    }

    /**
     * Initialisation. Create the block file loader from the specified file path.
     */
    private void initialise(String path) {
        log.info("Initialising Block Chain Parser on file path " + path);
        List<File> blockFiles = getBlockFiles(path);
        log.info("Total block files found: " + blockFiles.size());
        blockFileLoader = new BlockFileLoader(networkParameters, blockFiles);
    }

    /**
     * Retrieve a list of blocks from the block file loader.
     * @return
     */
    private List<DelegateBlock> parseBlocks() {
        List<DelegateBlock> blocks = new ArrayList<>();
        long startTimeMill = System.currentTimeMillis();

        // Iterate over the blocks in the data set.
        for (Block block : blockFileLoader) {
            log.info("Parsing block " + block.getHashAsString());
            DelegateBlock delegateBlock = new DelegateBlock(block);
            blocks.add(delegateBlock);
        }

        log.info("Parsed " + blocks.size() + " blocks");
        long endTimeMill = System.currentTimeMillis();
        long parseTimeSec = TimeUnit.MILLISECONDS.toSeconds(endTimeMill - startTimeMill);
        log.info("Parse took " + parseTimeSec + " seconds");

        return blocks;
    }

    /**
     * Search the file path for block files.
     * @return The list of files.
     */
    private List<File> getBlockFiles(String path) {
        List<File> blockFiles = null;
        log.info("Walking file path " + path);
        try {
            blockFiles = Files.walk(Paths.get(path))
                    .parallel()
                    .filter(BlockChainParser::isValidFile)
                    .map(Path::toString).map(File::new)
                    .collect(Collectors.toList());

            log.info("Found " + blockFiles.size() + " files in total");
            blockFiles.stream().forEach(file -> log.info("File: " + file.toString()));
        } catch (IOException e) {
            log.error("Exception while getting files", e);
        }

        return blockFiles;
    }

    /**
     * Determine if a file path is considered valid in this parser.
     * Ensure that only .dat files are collected.
     * Ensure the file name contains "blk". TODO: include rev files?
     *
     * Note: This does not perform a directory check.
     *
     * @param path The file path to validate.
     * @return If this path is valid or not.
     */
    public static boolean isValidFile(Path path) {
        boolean isValidFile = false;
        // Regular file filter
        if (Files.isRegularFile(path)) {
            // Extension filter
            String fileName = path.toString();
            String ext = FilenameUtils.getExtension(fileName);
            if (ext != null) {
                if (fileName.contains("blk") && ext.equals("dat")) {
                    isValidFile = true;
                }
            }
        }
        return isValidFile;
    }
}
