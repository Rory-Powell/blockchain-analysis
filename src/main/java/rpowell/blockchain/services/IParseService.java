package rpowell.blockchain.services;

import rpowell.blockchain.Transaction;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface IParseService {

    void writeTransactionsToDB(Set<Transaction> transactions);

    void parseBlockFiles(List<File> files);

    void parseBlockFiles();
}
