package network;

import info.blockchain.api.APIException;
import info.blockchain.api.HttpClient;
import info.blockchain.api.HttpClientInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Requests {
   private final static HttpClientInterface client = HttpClient.getInstance();

   private final static String BLOCK = "rawblock";    // Single Block
   private final static String TRANSACTION = "rawtx"; // Single Transaction
   private final static String CHARTS = "charts";     // Chart Data

   public static String getBlock(String hash)
           throws APIException, IOException {
      Map<String, String> params = new HashMap<>();
      params.put("block_hash", hash);
      return  client.get(BLOCK, params);
   }

}
