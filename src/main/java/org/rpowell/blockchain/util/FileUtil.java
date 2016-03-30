package org.rpowell.blockchain.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * Returns all the files in a directory.
     * @param directory	Path to the directory.
     * @return          A list of Files from the directory.
     */
    public static List<File> getFolderContents(String directory) {
        File file = new File(directory);
        return new ArrayList<>(FileUtils.listFiles(file, CanReadFileFilter.CAN_READ, DirectoryFileFilter.DIRECTORY));
    }
}
