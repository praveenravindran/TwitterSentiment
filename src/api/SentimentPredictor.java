package api;

import java.util.HashMap;


public class SentimentPredictor {
	
	public HashMap<String,String> predictSentiment(String str){
		
		String label="positive";
		String probability = "0.5";
		HashMap<String,String> hash = new HashMap<String,String> ();
		hash.put(label, probability);
		System.out.println(label);
		return hash;
		
		
	}
	
	public static void main(String args[]){
		
		SentimentPredictor obj = new SentimentPredictor();
		HashMap<String,String> hash = obj.predictSentiment("good");
		String prob = hash.get("positive");
		System.out.println(prob);
		
	}

}
