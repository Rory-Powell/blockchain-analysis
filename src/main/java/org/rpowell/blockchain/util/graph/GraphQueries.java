package org.rpowell.blockchain.util.graph;

import org.neo4j.graphdb.Label;

import static org.rpowell.blockchain.util.graph.GraphConstants.*;

public class GraphQueries {

    /**
     * The cypher query used to get the immediate community of a given address.
     * @param address   The address to detect the community for.
     * @param depth     The depth of the search.
     * @return          THe query.
     */
    public static String addressQuery(String address, int depth) {
        return  "MATCH (n:" + Labels.ADDRESS + ") " +
                "WHERE n." + Props.ADDR + " = '" + address + "' " +
                "OPTIONAL MATCH path=n-[*1.." + depth + "]-(c) " +
                "RETURN path LIMIT 1000";
    }

    /**
     * The Cypher query used to return all known addresses.
     * @param limit The limit of number of addresses to return.
     * @return      The query
     */
    public static String addressesQuery(int limit) {
        return "MATCH (n:Address) RETURN n.Addr LIMIT " + limit;
    }

    /**
     * The Cypher query used to get the owner ID's of a given address.
     * @param address   The address to find owner for.
     * @return          The query.
     */
    public static String ownerQuery(String address) {
        return "MATCH (n:Address {Addr:'" + address + "'})<-[:Same_Owner]-(wallet) RETURN DISTINCT ID(wallet)";
    }

    /**
     * The Cypher query used to get the addresses of an owner given it's ID.
     * @param ownerId   The ID of the owner.
     * @return          The addresses owned by the owner.
     */
    public static String addressesOfOwnerQuery(int ownerId) {
        return "MATCH (n:Wallet) where ID(n) = " + ownerId + " match n-[:Same_Owner]->(address) RETURN address.Addr";
    }

    /**
     * The Cypher query used to get the count of all addresses in the database.
     * @return  The query.
     */
    public static String addressCountQuery() {
        return getCountQuery(Labels.ADDRESS);
    }

    /**
     * The Cypher query used to get the count of all transactions in the database.
     * @return  The query.
     */
    public static String transactionCountQuery() {
        return getCountQuery(Labels.TRANSACTION);
    }

    /**
     * The Cypher query used to get the count of all owners in the database.
     * @return  The query.
     */
    public static String ownerCountQuery() {
        return getCountQuery(Labels.WALLET); // TODO : This is the tough one - Persistence logic will need changed
    }

    /**
     * The Cypher query used to get the count of all nodes in the database.
     * @return  The query.
     */
    public static String nodeCountQuery() {
        return "MATCH (n) RETURN count(n)";
    }

    /**
     * Using a given label, get the query to count the number of nodes with that label.
     * @param label The label to count.
     * @return      The query.
     */
    private static String getCountQuery(Label label) {
        return "Match (n:" + label + ") RETURN count(n)";
    }


}
