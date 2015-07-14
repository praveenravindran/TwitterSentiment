package preprocess;

import java.util.HashMap;

public class ProcessedDocument {
	
	public String label;
	public String text;
	public HashMap<String,Integer> featurizedText;
	
	public ProcessedDocument(String label,String text,HashMap<String,Integer>  featurizedText){
		this.label=label;
		this.text= text;
		this.featurizedText=featurizedText;
				
	}
	
	
}
