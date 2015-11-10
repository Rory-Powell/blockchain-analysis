package parsing;

import analysis.BlockAnalyser;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by rpowell on 08/11/15.
 */
public class BlockChainParser {

    // Logger
    private static final Logger log = LoggerFactory.getLogger(BlockChainParser.class);

    // The path to the block data set
    private String path;

    // The file loader
    private BlockFileLoader blockFileLoader;

    // The configuration context, stashed in thread local storage.
    private Context context;

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
        log.info("Initialising Block Chain Parser");
        NetworkParameters networkParameters = new MainNetParams();
        context = new Context(networkParameters);
        List<File> blockFiles = getBlockFiles();
        log.info("Total block files found: " + blockFiles.size());
        blockFileLoader = new BlockFileLoader(networkParameters, blockFiles);
    }

    /**
     * Retrieve a list of blocks from the block file loader.
     * @return
     */
    public List<Block> parseBlocks() {
        List<Block> blocks = new ArrayList<>();
        long startTimeMill = System.currentTimeMillis();

        // Iterate over the blocks in the data set.
        for (Block block : blockFileLoader) {
            blocks.add(block);
            log.info("Parsing block " + block.getHashAsString());
        }

        log.info("Parsed " + blocks.size() + " blocks");
        long endTimeMill = System.currentTimeMillis();
        long parseTimeSec = TimeUnit.MILLISECONDS.toSeconds(endTimeMill - startTimeMill);
        log.info("Parse took " + parseTimeSec + " seconds");
        return blocks;
    }

    public BlockFileLoader blockFileLoader() {
        return blockFileLoader;
    }

    /**
     * Search the file path for block files.
     * @return The list of files.
     */
    private List<File> getBlockFiles() {
        List<File> blockFiles = null;
        log.info("Walking file path for " + path);
        try {
            blockFiles = Files.walk(Paths.get(path))
                    .filter(BlockChainParser::isValidFile)
                    .map(path -> new File(path.toString()))
                    .collect(Collectors.toList());

            for (File file : blockFiles) {
                log.info("Found file " + file.toString());
            }
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
