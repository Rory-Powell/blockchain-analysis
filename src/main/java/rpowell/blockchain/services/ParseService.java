package rpowell.blockchain.services;

import rpowell.blockchain.Transaction;
import java.util.Set;

public interface ParseService {

    void writeTransactionsToDB(Set<Transaction> transactions);
}
