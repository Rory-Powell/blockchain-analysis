import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class App {

   public static void main(String[] args) {



   }

   // Arm the blockchain file loader.
   NetworkParameters np = new MainNetParams();
   List<File> blockChainFiles = new ArrayList<>();
   blockChainFiles.add(new File("/tmp/bootstrap.dat"));
   BlockFileLoader bfl = new BlockFileLoader(np, blockChainFiles);

   // Data structures to keep the statistics.
   Map<String, Integer> monthlyTxCount = new HashMap<>();
   Map<String, Integer> monthlyBlockCount = new HashMap<>();

   // Iterate over the blocks in the dataset.
   for (Block block : bfl) {

      // Extract the month keyword.
      String month = new SimpleDateFormat("yyyy-MM").format(block.getTime());

      // Make sure there exists an entry for the extracted month.
      if (!monthlyBlockCount.containsKey(month)) {
         monthlyBlockCount.put(month, 0);
         monthlyTxCount.put(month, 0);
      }

      // Update the statistics.
      monthlyBlockCount.put(month, 1 + monthlyBlockCount.get(month));
      monthlyTxCount.put(month, block.getTransactions().size() + monthlyTxCount.get(month));

   }

   // Compute the average number of transactions per block per month.
   Map<String, Float> monthlyAvgTxCountPerBlock = new HashMap<>();
   for (String month : monthlyBlockCount.keySet())
           monthlyAvgTxCountPerBlock.put(
   month, (float) monthlyTxCount.get(month) / monthlyBlockCount.get(month));
}
