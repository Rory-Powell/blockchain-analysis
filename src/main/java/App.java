import analysis.BlockAnalyser;
import org.bitcoinj.core.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class App {
   private static final Logger log = LoggerFactory.getLogger(App.class);

   public static void main(String[] args) {
      BlockAnalyser blockAnalyser = new BlockAnalyser();
      List<Block> blocks  = blockAnalyser.parseBlocksOriginalFormat();
      Map<Long, Integer> timeToCount = blockAnalyser.mapTransactionsToTime(blocks);
   }
}
