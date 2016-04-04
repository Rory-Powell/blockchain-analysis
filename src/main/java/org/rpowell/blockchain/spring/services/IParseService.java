package org.rpowell.blockchain.spring.services;

/**
 * Used to parse JSON files on disk and write them to the database.
 */
public interface IParseService {

    void writeJSONToDB();

}
