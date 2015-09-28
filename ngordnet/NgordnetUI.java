package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Dhruv Harendra Relwani
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In file = new In("./ngordnet/ngordnetui.config");
        String wordFile = file.readString();
        String countFile = file.readString();
        String synsetFile = file.readString();
        String hyponymFile = file.readString();
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wnet = new WordNet(synsetFile, hyponymFile);
        YearlyRecordProcessor yrp = new WordLengthProcessor(); 
        int start = Integer.MIN_VALUE, end = Integer.MAX_VALUE;
        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            try{
              switch (command) {
                  case "quit": 
                      return;
                  case "help":
                      In in = new In("./ngordnet/help.txt");
                      String helpStr = in.readAll();
                      System.out.println(helpStr);
                      break;  
                  case "range": 
                      int startDate = Integer.parseInt(tokens[0]); 
                      int endDate = Integer.parseInt(tokens[1]);
                      start = startDate;
                      end = endDate;
                      break;
                  case "count":
                      String word = tokens[0]; 
                      int year = Integer.parseInt(tokens[1]);
                      System.out.println(ngm.countInYear(word, year));
                      break;
                  case "hyponyms":
                      String wrd = tokens[0]; 
                      System.out.println(wnet.hyponyms(wrd));
                      break;
                  case "history":
                      Plotter.plotAllWords(ngm, tokens, start, end);
                      break;
                  case "hypohist":
                      Plotter.plotCategoryWeights(ngm, wnet, tokens, start, end);
                      break;
                  case "wordlength":
                      Plotter.plotProcessedHistory(ngm, start, end, yrp);
                      break;
                  default:
                      System.out.println("Invalid command.");  
                      break;
                }
                }catch (RuntimeException e) {
                  System.out.println("Invalid/ Extra Input");
                }
        }
    }
}
