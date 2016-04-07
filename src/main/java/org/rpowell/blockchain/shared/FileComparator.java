package org.rpowell.blockchain.shared;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator used to sort a list of files.
 * Treats the file name as a numerical value.
 */
public class FileComparator implements Comparator<File>{

    private String fileExtension;

    /**
     * Construct a file comparator.
     * @param fileExtension The extension to trim while performing the comparison.
     */
    public FileComparator(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public int compare(File file1, File file2) {
        String file1Name = file1.getName();
        int file1Size = Integer.parseInt(file1Name.substring(0, file1Name.length() - fileExtension.length()));

        String file2Name = file2.getName();
        int file2Size = Integer.parseInt(file2Name.substring(0, file2Name.length() - fileExtension.length()));

        return file1Size - file2Size;
    }
}
