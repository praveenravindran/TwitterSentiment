package preprocess;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextProcessor {
	public Twokenize twokenizer;
	public TextProcessor(){
		// Initialize the twitter tokenizer.
		this.twokenizer = new Twokenize();
	}
	
	public String[] whiteSpaceTokeninze(String str){
	//	ArrayList<String> array = new ArrayList<String> (); 
		String array[] = str.split("\\s+");
	
	return array;
	}
	
	public String[] twitterTokenize(String str){
		List<String> temp= Twokenize.tokenizeRawTweetText(str);
		String [] tokens = temp.toArray(new String[temp.size()]);
		return tokens;
	}
	
	
	public HashMap<String,Integer> featurize(String text,String tokenizeMethod){
		String tokens[];
		if(tokenizeMethod=="twitter"){
		tokens = this.twitterTokenize(text);
		}
		else{
			tokens= this.whiteSpaceTokeninze(text);
		}
		HashMap<String,Integer> wordCounts  = new HashMap<String,Integer>();
		//System.out.println(wordCounts.get("hello"));
		for(int i=0; i<tokens.length;i++){
			tokens[i] = tokens[i].toLowerCase();
			if(wordCounts.get(tokens[i])==null){
				wordCounts.put(tokens[i], 1);
			}
			else{
				int count = wordCounts.get(tokens[i]) +1;
				wordCounts.put(tokens[i], count);
			}
		}
		return wordCounts;
		}
	
	/*public static void main(String args[]){
		TextProcessor obj = new TextProcessor();
		HashMap<String,Integer> wordCounts=obj.featurize(obj.tokeninze("hello world world awesome"));
		System.out.println(wordCounts.toString());					
	}*/
	
	

}
