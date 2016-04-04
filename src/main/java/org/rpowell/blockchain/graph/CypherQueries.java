package org.rpowell.blockchain.graph;

import static org.rpowell.blockchain.graph.GraphConstants.*;

public class CypherQueries {

    public static String getAddressQuery(String address, int depth) {
        String query =  "MATCH (n:" + Labels.ADDRESS + ") WHERE n." + Properties.ADDR + " = '" + address
                        + "' OPTIONAL MATCH path=n-[*1.." + depth + "]-(c) RETURN path";
        return query;
    }
}
