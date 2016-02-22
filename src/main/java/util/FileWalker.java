package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

// Class to walk the file system and discover files.
public class FileWalker {
    private static final Logger log = LoggerFactory.getLogger(BlockParser.class);
    public static final String DAT_PATH = "/home/rpowell/dev/projects/final-project/resources";

    // Search the default path for files
    public static List<File> discoverFilesOnDefaultPath() {
        return discoverFilesOnPath(DAT_PATH);
    }

    // Search a given path for files
    public static List<File> discoverFilesOnPath(String path) {
        List<File> discoveredFiles = null;
        log.info("Walking file path " + path);

        try {
            // Walk the file path and discover any block files
            discoveredFiles = Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(Path::toString).map(File::new)
                    .collect(Collectors.toList());

            // Perform logging
            log.info("Found " + discoveredFiles.size() + " files in total");
            discoveredFiles.sort(File::compareTo);
        } catch (IOException e) {
            log.error("Exception while getting files", e);
        }

        // Return the list of found files
        return discoveredFiles;
    }
}
