package ngordnet;
public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
    	int totalNumberOfCounts = 0, wordlengthTimesCount = 0;
    	for (String word : yearlyRecord.words()) {
    		int count = yearlyRecord.count(word);
    		totalNumberOfCounts += count;
    		wordlengthTimesCount += word.length() * count; 
    	}
    	return wordlengthTimesCount/totalNumberOfCounts;
    } 
}
