import info.blockchain.api.APIException;
import network.Requests;

import java.io.IOException;

public class App {

   public static String BLOCK_HASH = "0000000000000bae09a7a393a8acded75aa67e46cb81f7acaa5ad94f9eacd103";

   public static void main(String[] args) {
      try {
         String block = Requests.getBlock(BLOCK_HASH);
      } catch (APIException | IOException e) {
         e.printStackTrace();
      }
   }
}
