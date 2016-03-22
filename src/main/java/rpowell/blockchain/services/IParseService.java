package rpowell.blockchain.services;

import java.io.File;
import java.util.List;

public interface IParseService {

    void parseBlockFiles(List<File> files);

    void parseBlockFiles();
}
