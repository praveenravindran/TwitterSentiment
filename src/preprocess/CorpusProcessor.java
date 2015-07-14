package preprocess;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.CharEncoding;

import preprocess.TextProcessor;
import preprocess.ProcessedDocument.*;

public class CorpusProcessor {
	public ArrayList<String> mostFreqWords;
	public ArrayList<ProcessedDocument> documents = new ArrayList<ProcessedDocument>();
	public HashMap<String,Integer> corpWordFreqMap= new HashMap<String,Integer>();
	
	private TextProcessor textProcessor = new TextProcessor();
	public CorpusProcessor(){
	
	}
	
	public void readAndFeaturizeDataSet(String filename) throws IOException{
		// getting the file and featurizing it
	//	String fileName="twitter_testing.csv";
		//File file = new File(fileName);
		File csvData = new File(filename);
		
			//CSVParser parser = CSVParser.parse(csvData);
		    //CSVParser.p
		CSVParser parser=CSVParser.parse(csvData,java.nio.charset.Charset.defaultCharset(),CSVFormat.DEFAULT);
		
		int i=0;
			 for (CSVRecord csvRecord : parser) {
				i+=1;
				if(i==1)
					continue;
				String label=csvRecord.get(0);
				String text = csvRecord.get(1);
				HashMap<String,Integer> featurizedText = this.textProcessor.featurize(text,"twitter");
				ProcessedDocument processedDoc = new ProcessedDocument(label,text,featurizedText);
				documents.add(processedDoc);
				//System.out.println(featurizedText);
				//System.out.println(csvRecord);
				 //List<CSVRecord> array = parser.getRecords();
			 }
			 
			 System.out.println(documents.get(89).text);
			 System.out.println(documents.get(89).featurizedText);
			
		}
	
	
	public void getMostFreqWords(int threshHold){
		HashMap<String,Integer> wordFreqMap = new HashMap<String,Integer>();
		for(ProcessedDocument document:this.documents){
			HashMap<String,Integer> tempHash = document.featurizedText;
			Iterator it = tempHash.entrySet().iterator();
			while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        //System.out.println(pair.getKey() + " = " + pair.getValue());
		        String word = (String) pair.getKey();
		        int count = (Integer) pair.getValue();
		        
		        // Update the global counts dict.
		        if(wordFreqMap.get(word)==null)
		        	wordFreqMap.put(word, tempHash.get(word));
		        else
		        {
		        	wordFreqMap.put(word, wordFreqMap.get(word)+tempHash.get(word));
		        }
		        }
			}
		this.corpWordFreqMap= wordFreqMap;
		// finding the words that occr above the threshold
		Iterator it = wordFreqMap.entrySet().iterator();
		ArrayList<String> wordsGreater = new ArrayList<String>();
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        String word = (String) pair.getKey();
	        int count = (Integer) pair.getValue();
	        if(count>threshHold){
	        	wordsGreater.add(word);
	        }
		}
		/*System.out.println(wordsGreater.size());
		System.out.println(wordsGreater.get(1000));
		System.out.println(wordsGreater.get(900));
		System.out.println(wordsGreater.get(600));*/
		this.mostFreqWords= wordsGreater;
		System.out.println(wordsGreater.size());
	    }
	
	public void filterCorpusWithImportantWords(){
		
		// Building a temporary hashMap of the most important words in the corpus.
		
		HashMap<String,Boolean> impWordsMap = new HashMap<String,Boolean>();
		for(String word:this.mostFreqWords){
			impWordsMap.put(word, true);
		}
		for(ProcessedDocument document:this.documents){
			HashMap<String,Integer> tempHash = document.featurizedText;
			HashMap<String,Integer> modifiedFeatures = new HashMap<String,Integer>();
			Iterator it = tempHash.entrySet().iterator();
			while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        //System.out.println(pair.getKey() + " = " + pair.getValue());
		        String word = (String) pair.getKey();
		        if(impWordsMap.get(word)!=null){
		        	modifiedFeatures.put(word, (Integer) pair.getValue());
		        }
			}
			document.featurizedText=modifiedFeatures;
			//System.out.println(document.text);
			//System.out.println(document.featurizedText);
		}
		
		
	}
	
	
	public static void main(String args[]) throws IOException{
		CorpusProcessor blah = new CorpusProcessor();
		blah.readAndFeaturizeDataSet("/home/pradeep/javaWorkspace/TwitterSentiment/src/data/twitter_training.csv");
		blah.getMostFreqWords(16);
		blah.filterCorpusWithImportantWords();
		System.out.println(blah.documents.get(89).featurizedText);
		
		System.out.println(blah.corpWordFreqMap.get("nikki"));
		
	}
}
