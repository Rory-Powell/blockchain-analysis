package org.rpowell.blockchain.graph;

import static org.rpowell.blockchain.graph.GraphConstants.*;

public class CypherQueries {

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

    public static String ownerQuery(String address) {
        return "MATCH (n:Address {Addr:'" + address + "'})<-[:Same_Owner]-(wallet) RETURN DISTINCT ID(wallet)";
//        return "MATCH (n:Address {Addr:'16XQkgZgdxRe3MqK7nGQx8yfbw8bdF19ZT'})<-[:Same_Owner]-(wallet) RETURN ID(wallet)";
    }

    public static String addressesOfOwnerQuery(int ownerId) {
        return "MATCH (n:Wallet) where ID(n) = " + ownerId + " match n-[:Same_Owner]->(address) RETURN address.Addr";
//        return "MATCH (n:Wallet) where ID(n) = 15 match n-[:Same_Owner]->(address) RETURN address";
    }

    public static String addressCountQuery() {
        return null;
    }

    public static String transactionCountQuery() {
        return null;
    }

    public static String nodeCountQuery() {
        return null;
    }

    // TODO : This is the tough one - Persistence logic will need changed
    public static String ownerCountQuery() {
        return null;
    }
}
