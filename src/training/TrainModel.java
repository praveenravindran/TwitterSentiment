package training;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import weka.core.SparseInstance;
import weka.core.DenseInstance;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
//import weka.core.FastVector;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import preprocess.CorpusProcessor;
import preprocess.ProcessedDocument;
public class TrainModel {
	
	private CorpusProcessor corpusProcessor;
	private int corpusSize;
	private ArrayList<String> vocabulary;
	private HashMap<String,Integer> vocabIndexMap;
	private ArrayList<Attribute> featVectFormat;
	public Instances trainingDataset;
	public Classifier model;
	
	public TrainModel(){
		// to do later
	}
	
	public void buildTrainingCorpus() throws IOException{
		/* builds the training corpus with the features extracted with the frequent words*/
		this.corpusProcessor = new CorpusProcessor();
		this.corpusProcessor.readAndFeaturizeDataSet("/home/pradeep/javaWorkspace/TwitterSentiment/src/data/twitter_training.csv");
		this.corpusProcessor.getMostFreqWords(5);
		this.corpusProcessor.filterCorpusWithImportantWords();
		corpusSize = this.corpusProcessor.mostFreqWords.size();
		vocabulary = this.corpusProcessor.mostFreqWords;
		
		/*
		 * Build a HashMap that keeps track of all words and its corresponding index in the vocabulary.
		 */
		this.vocabIndexMap = new HashMap<String,Integer>();
		int i=0;
		for(String word : vocabulary)
		{
			this.vocabIndexMap.put(word, i);
			i+=1;
		}
	}
	
	public void declareFeatureVector(){
		// declares the feature vector format
		ArrayList<Attribute> docVector = new ArrayList<Attribute>(this.corpusSize+1);
		for(String word:this.vocabulary){
			Attribute wordAtt = new Attribute(word);
			docVector.add(wordAtt);
		}
		ArrayList<String> labelVec = new ArrayList<String>();
		labelVec.add("negative");
		labelVec.add("positive");
		Attribute classAttribute = new Attribute("sentiment",labelVec);
		docVector.add(classAttribute);
		this.featVectFormat=docVector;
	}
	
	public SparseInstance buildVectorForDoc(HashMap<String,Integer> features, String label){
	//	SparseInstance docInstance = new SparseInstance(this.corpusSize+1);
		int index=0;
		ArrayList<Double> attValues = new ArrayList<Double>(); 
		ArrayList<Integer>indices = new ArrayList<Integer>();
		
		/*
		 * Get the index in the vocabulary of each word in the document.
		 */
		for(String word : features.keySet())
				{
					int vInd =  this.vocabIndexMap.get(word);
					indices.add(vInd);
					attValues.add(1.0);
				}
		/*
		 * Convert the arraylists into arrays.
		 */
		int size = attValues.size();
		double[] attValuesArray = new double[size];
		int[] indicesArray = new int[size];
		
		for(int i=0;i<size;i++)
		{
			attValuesArray[i] = attValues.get(i);
			indicesArray[i] = indices.get(i);
		}
		
		/*
		 * Create a sparse Instance.
		 */
		SparseInstance docInstance = new SparseInstance(1.0,attValuesArray,indicesArray,this.corpusSize);
		docInstance.setValue((Attribute)this.featVectFormat.get(this.corpusSize), label);
		return docInstance;
		}
	
	public void buildWekaTrainingSet(){
		// build vectors for every tweet and make a training matrix
		this.declareFeatureVector();
		Instances trainingSet = new Instances("tweettrainingset",this.featVectFormat,this.corpusProcessor.documents.size());
		trainingSet.setClassIndex(this.corpusSize);
		int i =0;
		for(ProcessedDocument document:this.corpusProcessor.documents){
			SparseInstance docInstance = this.buildVectorForDoc(document.featurizedText, document.label);
			Attribute classAtt = (Attribute)this.featVectFormat.get(this.corpusSize);
			//System.out.println(docInstance.value(classAtt));
			//System.out.println(document.featurizedText);
			//System.out.println(docInstance);
			
			/*
			 * 
			 */
			/*
			for(String word : document.featurizedText.keySet())
			{
				System.out.println(word + "  " + this.vocabIndexMap.get(word));
			}
			*/
			//System.out.println(document.label);
			trainingSet.add(docInstance);
			//System.out.println(i);
			i++;
		}
		System.out.println(trainingSet.numClasses());
		System.out.println(trainingSet.numAttributes());
		//System.out.println(trainingSet.c
		this.trainingDataset = trainingSet;
	}
	
	public void buildModel()
	{
		/*
		 * Main function that takes the actual training dataset and trains the model.
		 */
		this.model = (Classifier)new NaiveBayes();
		try {
			System.out.println("Training the Naive Bayes classifier.");
			this.model.buildClassifier(this.trainingDataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void evaluateModelOnTrainingSet()
	{
		/*
		 * Evaluate the model on the training set.
		 */
		 // Test the model
		 Evaluation eTest;
		try {
			eTest = new Evaluation(this.trainingDataset);
			eTest.evaluateModel(this.model,this.trainingDataset);
			/*
			 * Print classification stats.
			 */
			// Print the result à la Weka explorer:
			 String strSummary = eTest.toSummaryString();
			 System.out.println(strSummary);
			 
			 // Get the confusion matrix
			 double[][] cmMatrix = eTest.confusionMatrix();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void trainModel() {
		// step1. build training corpus
		try {
			this.buildTrainingCorpus();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// step 2. format extracted features into weka specific training data format
		this.buildWekaTrainingSet();
		// step3. train the model with weka specific training data format
		
	}
		public static void main(String[] args) {
			TrainModel model = new TrainModel();
			model.trainModel();
		// TODO Auto-generated method stub
			model.buildModel();
			model.evaluateModelOnTrainingSet();
	}

}
