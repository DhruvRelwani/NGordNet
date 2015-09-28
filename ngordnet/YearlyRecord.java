package ngordnet;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.TreeSet;

public class YearlyRecord {
    private HashMap<String, Integer> wordToCount, wordToRank;
    private HashMap<Integer, Set<String>> countToWord;
    private boolean cached = false;
    private int wordNumber = 0;

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        this.wordToCount = new HashMap<String, Integer>();
        this.wordToRank = new HashMap<String, Integer>();
        this.countToWord = new HashMap<Integer, Set<String>>();
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        this.wordToCount = new HashMap<String, Integer>();
        this.countToWord = new HashMap<Integer, Set<String>>();
        this.wordToRank = new HashMap<String, Integer>();
        for (String word : otherCountMap.keySet()) {
            this.put(word, otherCountMap.get(word));
        }
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        return this.wordToCount.get(word);
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        if (wordToCount.values() != null && wordToCount.values().contains(count)) {
            this.countToWord.get(count).add(word);
        } else {
            Set<String> wordset = new HashSet<String>();
            wordset.add(word);
            this.countToWord.put(count, wordset);
        }
        this.wordToCount.put(word, count);
        this.wordNumber += 1;
        this.cached = false;
    } 

    /** Returns the number of words recorded this year. */
    public int size() {
        return this.wordNumber;
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        Collection<String> words = new ArrayList<String>();
        TreeSet<Integer> keyset = new TreeSet<Integer>();
        keyset.addAll(countToWord.keySet());
        for (Integer count : keyset) {
            words.addAll(this.countToWord.get(count));
        }
        return words;
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        Collection<Number> counts = new ArrayList<Number>();
        for (String word : this.words()) {
            counts.add(wordToCount.get(word));
        }
        return counts;
    } 

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        if (!cached) {
            int ctr = this.wordNumber;
            for (String wordTemp : this.words()) {
                wordToRank.put(wordTemp, ctr);
                ctr -= 1;
            }
            this.cached = true;
        }
        return wordToRank.get(word);
    }
} 
