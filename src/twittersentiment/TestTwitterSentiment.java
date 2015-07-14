package twittersentiment;

import weka.core.Attribute;
import weka.core.FastVector;

public class TestTwitterSentiment {
	
	public static void main(String args[]){
		
		Attribute Attribute1 = new Attribute("hi");
		 Attribute Attribute2 = new Attribute("yello");
		 
		 // Declare a nominal attribute along with its values
		 FastVector fvNominalVal = new FastVector(3);
		 fvNominalVal.addElement(“blue”);
		 fvNominalVal.addElement(“gray”);
		 fvNominalVal.addElement(“black”);
		 Attribute Attribute3 = new Attribute(“aNominal”, fvNominalVal);
		 
		 // Declare the class attribute along with its values
		 FastVector fvClassVal = new FastVector(2);
		 fvClassVal.addElement(“positive”);
		 fvClassVal.addElement(“negative”);
		 Attribute ClassAttribute = new Attribute(“theClass”, fvClassVal);
		 
		 // Declare the feature vector
		 FastVector fvWekaAttributes = new FastVector(4);
		 fvWekaAttributes.addElement(Attribute1);
		 fvWekaAttributes.addElement(Attribute2);
		 fvWekaAttributes.addElement(Attribute3);
		 fvWekaAttributes.addElement(ClassAttribute);
	}

}
