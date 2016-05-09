package org.rpowell.blockchain.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rpowell.blockchain.domain.*;
import org.rpowell.blockchain.services.http.IBlockchainHttpService;
import org.rpowell.blockchain.util.PropertyLoader;
import org.rpowell.blockchain.util.file.FileComparator;
import org.rpowell.blockchain.services.IFetcherService;
import org.rpowell.blockchain.util.file.FileUtil;
import org.rpowell.blockchain.util.StringConstants;
import org.rpowell.blockchain.util.graph.DownloadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FetcherServiceImpl implements IFetcherService {

    public static Block latestBlockOnDisk;

    private static final Logger log = LoggerFactory.getLogger(FetcherServiceImpl.class);

    @Autowired
    private IBlockchainHttpService blockchainHttpService;

    private String JSON_PATH = PropertyLoader.loadProperty("json.path");

    private final ObjectMapper mapper = new ObjectMapper();
    private int count = 0;

    // For use by spring
    protected FetcherServiceImpl() {}

    /**
     * Download and write the blockchain to disk as JSON format.
     * @param JsonPath  The path to write files to.
     */
    public void writeBlockchainToJSON(String JsonPath, int blockCount) {
        // Ensure directory exists
        File file = new File(JsonPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        // Retrieve files already on disk
        List<File> jsonFiles = FileUtil.getFolderContents(JsonPath);

        // Sort the list by numeric file name
        Collections.sort(jsonFiles, new FileComparator(StringConstants.JSON_FILE_EXT));

        // Retrieve the latest block on the blockchain
        LatestBlock latestBlock = blockchainHttpService.getLatestBlock();
        Block latestNetworkBlock = blockchainHttpService.getBlockByHash(latestBlock.getHash());

        initialiseDownload(jsonFiles, latestNetworkBlock, blockCount);
    }

    /**
     * Initialise the blockchain download.
     * @param jsonFiles             The existing files retrieved from disk.
     * @param latestNetworkBlock    The latest block from the blockchain.
     */
    public void initialiseDownload(List<File> jsonFiles, Block latestNetworkBlock, int blockCount) {
        Block genesisBlock = blockchainHttpService.getBlockByHash(StringConstants.GENESIS_BLOCK_HASH);

        if (jsonFiles.isEmpty()) {
            log.info(StringConstants.LINE_BREAK);
            log.info("Starting blockchain download");
            log.info(StringConstants.LINE_BREAK);

            downloadBlocks(latestNetworkBlock, genesisBlock.getBlock_index(), blockCount);
        } else {
            log.info(StringConstants.LINE_BREAK);
            log.info("Downloading new blocks. Please let this process complete.");
            log.info(StringConstants.LINE_BREAK);

            latestBlockOnDisk = getLatestBlockOnDisk(jsonFiles);

            if (latestNetworkBlock.getBlock_index() == latestBlockOnDisk.getBlock_index()) {
                log.info("Already up to date.");
            } else {
                downloadBlocks(latestNetworkBlock, latestBlockOnDisk.getBlock_index(), blockCount);
            }
        }
    }

    /**
     * Download and write the blockchain to disk as JSON format.
     */
    @Override
    public void writeBlockchainToJSON() {
        writeBlockchainToJSON(JSON_PATH, DownloadStatus.FULL);
    }

    @Override
    public void writeBlockchainToJSON(int blockCount) {
        writeBlockchainToJSON(JSON_PATH, blockCount);
    }

    /**
     * Download blocks to disk starting from the given block and
     * working backwards.
     * @param startBlock The block to start downloading from.
     */
    public void downloadBlocks(Block startBlock, long stopIndex, int blockCount) {
        int downloadCount = 0;
        count = 0;
        while (startBlock.getBlock_index() > stopIndex) {
            try {
                long index = startBlock.getBlock_index();
                File newFile = new File(JSON_PATH + index + StringConstants.JSON_FILE_EXT);

                filterInvalidTransactions(startBlock);

                mapper.writeValue(newFile, startBlock);
                log.info("Writing file " + newFile.toString());
                log.info("Blocks downloaded: " + count);
                log.info("Total Blocks: " + (index - stopIndex));
                log.info(StringConstants.LINE_BREAK);

                startBlock = blockchainHttpService.getBlockByHash(startBlock.getPrev_block());

                count++;

                // Limit the download if required
                if (blockCount != DownloadStatus.FULL) {
                    downloadCount++;
                    if (downloadCount == blockCount) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("Error downloading block", e);
                log.error("Sleeping for one minute", e);
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                } catch (InterruptedException e1) {
                    log.error("Interrupted during sleep");
                }
            }
        }
    }

    /**
     * Some blockchain data is lossy. Remove the transactions from the block that are missing information about
     * the addresses of inputs / outputs. This may result in data loss.
     *
     * @param block The block to filter.
     */
    private void filterInvalidTransactions(Block block) {
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : block.getTx()) {
            boolean filterTransaction = false;

            for (Input input : transaction.getInputs()) {
                if (input.getPrev_out() == null || input.getPrev_out().getAddr() == null) {
                    filterTransaction = true;
                }
            }

            for (Output output : transaction.getOut()) {
                if (output.getAddr() == null) {
                    filterTransaction = true;
                }
            }

            if (!filterTransaction) {
                filteredTransactions.add(transaction);
            }
        }

        block.setTx(filteredTransactions);
    }

    /**
     * Get the latest block on disk.
     * Assumes the list of files are sorted from low to high.
     *
     * @param jsonFiles The files to search.
     * @return          The latest block.
     */
    private Block getLatestBlockOnDisk(List<File> jsonFiles) {
        Block lastBlock = null;

        if (jsonFiles != null && !jsonFiles.isEmpty()) {
            File lastFile = jsonFiles.get(jsonFiles.size() - 1);

            try {
                lastBlock = mapper.readValue(lastFile, Block.class);
            } catch (IOException e) {
                log.error("Error getting latest block", e);
            }
        }

        return lastBlock;
    }
}
