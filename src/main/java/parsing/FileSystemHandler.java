package parsing;

import data.DelegateBlock;
import org.bitcoinj.core.Block;

import java.io.File;
import java.util.List;

/**
 * Created by rpowell on 21/11/15.
 */
public class FileSystemHandler {

    public static final String DAT_PATH ="/home/rpowell/dev/projects/final-project/resources";
    public static final String JSON_PATH = "/home/rpowell/dev/projects/final-project/output";

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

    public List<Block> parseBlockOriginalFormat() {
        List<File> filesOnDatPath = FileWalker.discoverFilesOnPath(datPath);
        List<Block> blocks = blockParser.parseBlocksOriginalFormat(filesOnDatPath);
        return blocks;
    }

    public void parseAndWriteBlocks() {
        List<File> filesOnDatPath = FileWalker.discoverFilesOnPath(datPath);
        blockParser.writeFilesAsBlocks(filesOnDatPath, jsonPath);
    }

    public List<DelegateBlock> readBlocks() {
        return blockParser.readFilesAsBlocks(jsonPath);
    }
}
