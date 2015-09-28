package ngordnet;
import edu.princeton.cs.introcs.In;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Collection;

public class NGramMap {
    private Map<Integer, YearlyRecord> yearToRecord = new HashMap<Integer, YearlyRecord>();
    private TimeSeries<Long> yearToTotalWords = new TimeSeries<Long>();

    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsYearCount = new In(wordsFilename);
        while (wordsYearCount.hasNextLine()) {
            String[] temp = wordsYearCount.readLine().split("\t");
            String word = temp[0];
            Integer year = Integer.parseInt(temp[1]);
            Integer count = Integer.parseInt(temp[2]);
            // If there is already a record for  that year
            if (yearToRecord.keySet() != null && yearToRecord.containsKey(year)) {
                this.yearToRecord.get(year).put(word, count);
            } else {
                YearlyRecord record = new YearlyRecord();
                record.put(word, count);
                yearToRecord.put(year, record);
            }
        }
        In yearTotalCount = new In(countsFilename);
        while (yearTotalCount.hasNextLine()) {
            String[] container = yearTotalCount.readLine().split(",");
            Integer year = Integer.parseInt(container[0]);
            Long count = Long.parseLong(container[1]);
            yearToTotalWords.put(year, count);
        }
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        YearlyRecord record = this.yearToRecord.get(year);
        if (!record.words().contains(word)) {
            return 0;
        }
        return record.count(word);
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord newRecord = new YearlyRecord();
        for (String word : yearToRecord.get(year).words()) {
            newRecord.put(word, yearToRecord.get(year).count(word));
        }
        return newRecord;
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return this.yearToTotalWords;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> yearToCount = new TimeSeries<Integer>();
        for (int year = startYear; year <= endYear; year += 1) {
            YearlyRecord record = this.yearToRecord.get(year);
            if (record != null && record.words().contains(word)) {
                yearToCount.put(year, record.count(word));
            }
        }
        return yearToCount;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        TimeSeries<Integer> yearToCount = new TimeSeries<Integer>();
        TreeSet<Integer> years = new TreeSet<Integer>();
        years.addAll(yearToRecord.keySet());
        for (Integer year : years) {
            YearlyRecord record = this.yearToRecord.get(year);
            if (record != null && record.words().contains(word)) {
                yearToCount.put(year, record.count(word));
            }
        }
        return yearToCount;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> wordHistory = countHistory(word, startYear, endYear);
        TimeSeries<Long> totalWordsHistory = new TimeSeries(yearToTotalWords, startYear, endYear);
        return wordHistory.dividedBy(totalWordsHistory);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        return countHistory(word).dividedBy(yearToTotalWords);
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, int start, int end) {
        TimeSeries<Double> relativeFrequencies = new TimeSeries<Double>();
        for (String word : words) {
            relativeFrequencies = relativeFrequencies.plus(weightHistory(word, start, end));
        }
        return relativeFrequencies;
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> relativeFrequencies = new TimeSeries<Double>();
        for (String word : words) {
            relativeFrequencies = relativeFrequencies.plus(weightHistory(word));
        }
        return relativeFrequencies;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */ 
    public TimeSeries<Double> processedHistory(int start, int end, YearlyRecordProcessor yrp) {
        TimeSeries<Double> processed = new TimeSeries<Double>();
        for (int year = start; year <= end; year += 1) {
            processed.put(year, yrp.process(yearToRecord.get(year)));
        }
        return processed;
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> processed = new TimeSeries<Double>();
        TreeSet<Integer> years = new TreeSet<Integer>();
        years.addAll(yearToRecord.keySet());
        for (Integer year : years) {
            processed.put(year, yrp.process(yearToRecord.get(year)));
        }
        return processed;
    } 
}
