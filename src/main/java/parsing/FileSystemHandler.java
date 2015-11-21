package parsing;

import data.DelegateBlock;

import java.io.File;
import java.util.List;

/**
 * Created by rpowell on 21/11/15.
 */
public class FileSystemHandler {

    public static final String DAT_PATH ="/home/rpowell/dev/resources/blockchain-analysis/blocks/all";
    public static final String JSON_PATH = "/home/rpowell/dev/resources/blockchain-analysis/output/json_blocks";
    private final String datPath;
    private final String jsonPath;

    BlockParser blockParser = new BlockParser();

    public FileSystemHandler() {
        this(DAT_PATH, JSON_PATH);
    }

    public FileSystemHandler(String datPath, String jsonPath) {
        this.datPath = datPath;
        this.jsonPath = jsonPath;
    }

    public void parseAndWriteBlocks() {
        List<File> filesOnDatPath = FileWalker.discoverFilesOnPath(datPath);
        blockParser.writeFilesAsBlocks(filesOnDatPath, jsonPath);
    }

    public List<DelegateBlock> readBlocks() {
        return blockParser.readFilesAsBlocks(jsonPath);
    }
}
