package org.rpowell.blockchain.graph;

import java.util.HashMap;
import java.util.Map;

import static org.rpowell.blockchain.graph.GraphConstants.*;

public class CypherQueries {

    public static String getAddressQuery() {
        return "MATCH (n:" + Labels.ADDRESS + ") WHERE n." + Properties.ADDR + " = '{address}' OPTIONAL MATCH path=n-[*1..{depth}]-(c) RETURN path";
    }

    public static Map<String, Object> getAddressQueryParams(String address, int depth) {
        Map<String, Object> params = new HashMap<>();

        params.put("address", address);
        params.put("depth", depth);

        return params;
    }

}
