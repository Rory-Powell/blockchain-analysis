package parsing;

import data.DelegateBlock;
import data.SimpleTransaction;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.NetworkParameters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by rpowell on 21/11/15.
 */
public class FileSystemHandler {

    public static final String DAT_PATH ="/home/rpowell/dev/projects/final-project/resources";
    public static final String JSON_PATH = "/home/rpowell/dev/projects/final-project/output";
    public static final String TAB_PATH = "/home/rpowell/dev/projects/final-project/";

    private final String datPath;
    private final String jsonPath;
    private BlockParser blockParser;

    public FileSystemHandler(NetworkParameters netParameters) {
        this(DAT_PATH, JSON_PATH, netParameters);
    }

    public FileSystemHandler(String datPath, String jsonPath, NetworkParameters netParams) {
        this.datPath = datPath;
        this.jsonPath = jsonPath;
        this.blockParser = new BlockParser(netParams);
    }

    public void writeTabDelimitedTransactions(List<SimpleTransaction> transactions) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(TAB_PATH + "blockchain.txt")));
        StringBuilder stringBuilder = new StringBuilder();

//        for (SimpleTransaction transaction : transactions) {
//            stringBuilder.append(transaction.getFrom()).append("\t").append(transaction.getFrom()).append("\n");
//        }

        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.close();
    }

    public List<Block> parseBlockOriginalFormat() {
        List<File> filesOnDatPath = FileWalker.discoverFilesOnPath(datPath);
        List<Block> blocks = null;
        try {
            blocks = blockParser.parseBlocksOriginalFormat(filesOnDatPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
