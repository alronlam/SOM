package drivers;
import htmlgenerator.DigitsHTMLGenerator;
import htmlgenerator.HTMLGenerator;
import htmlgenerator.InternetHTMLGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import som.SOMFacade;
import som.labeler.NearestNeighborLabeler;
import som.labeler.SOMNetworkLabeler;
import som.network.DefaultNetwork;
import weka.core.Instances;
import converters.DigitsNeuronsToInstancesConverter;
import converters.InternetNeuronsToInstancesConverter;
import converters.NeuronsToInstancesConverter;
import datasets.DatasetManager;
import datasets.DatasetManagerFactory;

public class TestDriver {

    public static int maxIterations;
    public static double initialNeighborhoodRadius;
    public static double neighborhoodLambda;
    public static boolean useRadiusDecay;
    
    public static void main(String[] args) {

//	runSOMOnInternet(100, 15, 15, 0.75, "Internet_15x15_100it_75_t.html", true);

	runSOMOnInternet(10, 15, 15, 0.4, "Internet_15x15_10it_4_t.html", true);
	runSOMOnInternet(40, 15, 15, 0.4, "Internet_15x15_40it_4_t.html", true);

    }
    
    
    
    private static void runSOMOnDigits(int maxIterations, int rows, int cols, double learningRate, String outputFileName, boolean useRadiusDecay){
	TestDriver.useRadiusDecay = useRadiusDecay;
	TestDriver.maxIterations = maxIterations;
	TestDriver.initialNeighborhoodRadius = rows/2;
	TestDriver.neighborhoodLambda =  maxIterations/Math.log(initialNeighborhoodRadius);
	
	SOMFacade som = new SOMFacade();
	som.setRows(rows);
	som.setCols(cols);
	som.setLearningRate(learningRate);
	som.setNumIterations(maxIterations);
	
	DatasetManager digitsDM = DatasetManagerFactory.create(DatasetManagerFactory.DIGITS);
	
	int numAttributes = digitsDM.getNumAttributesWithoutClass();
	double[] maxWeights = new double[numAttributes];
	for (int i = 0; i < numAttributes; i++)
	    maxWeights[i] = 16;
	
	NeuronsToInstancesConverter digitsConverter = new DigitsNeuronsToInstancesConverter();

	SOMNetworkLabeler somLabeler = new NearestNeighborLabeler(3);

	HTMLGenerator generator = new DigitsHTMLGenerator();
	
	if(runSOM(digitsDM, som, 10, maxWeights, digitsConverter, somLabeler, generator, outputFileName))
	    System.out.println("SOM + K-Means ran successfully.");
	else
	    System.out.println("Unsuccessful.");
	
    }
    
    private static void runSOMOnInternet(int maxIterations, int rows, int cols, double learningRate, String outputFileName, boolean useRadiusDecay){
	TestDriver.useRadiusDecay = useRadiusDecay;
	TestDriver.maxIterations = maxIterations;
	TestDriver.initialNeighborhoodRadius = rows/2;
	TestDriver.neighborhoodLambda =  maxIterations/Math.log(initialNeighborhoodRadius);
	
	SOMFacade som = new SOMFacade();
	som.setRows(rows);
	som.setCols(cols);
	som.setNumIterations(maxIterations);
	som.setLearningRate(learningRate);
	
	DatasetManager internetDM = DatasetManagerFactory.create(DatasetManagerFactory.INTERNET);
	
	int numAttributes = internetDM.getNumAttributesWithoutClass();
	double[] maxWeights = new double[numAttributes];
	for (int i = 0; i < numAttributes; i++)
	    if(i == 0)
		maxWeights[i] = 80;
	    else
		maxWeights[i] = 1;
	
	NeuronsToInstancesConverter digitsConverter = new InternetNeuronsToInstancesConverter();

	SOMNetworkLabeler somLabeler = new NearestNeighborLabeler(3);

	HTMLGenerator generator = new InternetHTMLGenerator();
	
	if(runSOM(internetDM, som, 10, maxWeights, digitsConverter, somLabeler, generator, outputFileName))
	    System.out.println("SOM + K-Means ran successfully.");
	else
	    System.out.println("Unsuccessful.");
	
    }


    private static boolean runSOM(DatasetManager dataManager, SOMFacade som, int numClusers, double[] maxWeights, NeuronsToInstancesConverter neuronsToInstancesConverter, SOMNetworkLabeler somLabeler, HTMLGenerator htmlGenerator, String outputFileName ) {
	// Run SOM
	int numAttributes = dataManager.getNumAttributesWithoutClass();

	DefaultNetwork network = som.runSOM(numAttributes, maxWeights, dataManager.getDataWithoutClass());

	// Convert the SOM network to Instances (Weka class)
	Instances instances = neuronsToInstancesConverter.convert(network, "Digits");

	// Label the nodes in the network
	LinkedList<LinkedList<String>> labeledSOM = somLabeler.labelNetwork(network, dataManager);

	// Run K-Means
	KMeansFacade kMeans = new KMeansFacade();
	
	int[] clusters = kMeans.runKMeans(network, numClusers, instances);

	// Genrate HTML File
	String html = htmlGenerator.generateHTML(clusters, labeledSOM);
	try {
	    FileWriter fw = new FileWriter(new File(outputFileName));
	    fw.write(html);
	    fw.close();
	    return true;
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
	
	return false;
    }
    
//    private static void runSOMOnOurDataset() {
//
//	// Prepare the dataset
//	DatasetManager digitsDM = DatasetManagerFactory
//		.create(DatasetManagerFactory.DIGITS);
//
//	// Run SOM
//	SOMFacade som = new SOMFacade();
//	som.setRows(20);
//	som.setCols(20);
//	som.setNumIterations(10);
//
//	int numAttributes = digitsDM.getNumAttributesWithoutClass();
//	double[] maxWeights = new double[numAttributes];
//	for (int i = 0; i < numAttributes; i++)
//	    maxWeights[i] = 16;
//
//	DefaultNetwork network = som.runSOM(numAttributes, maxWeights,
//		digitsDM.getDataWithoutClass());
//
//	// Convert the SOM network to Instances (Weka class)
//	NeuronsToInstancesConverter digitsConverter = new DigitsNeuronsToInstancesConverter();
//	Instances instances = digitsConverter.convert(network, "Digits");
//
//	// Label the nodes in the network
//	SOMNetworkLabeler somLabeler = new NearestNeighborLabeler(8);
//	LinkedList<LinkedList<String>> labeledSOM = somLabeler.labelNetwork(
//		network, digitsDM);
//
//	// Run K-Means
//	KMeansFacade kMeans = new KMeansFacade();
//	int[] clusters = kMeans.runKMeans(network, 10, instances);
//
//	// Genrate HTML File
//	HTMLGenerator generator = new DigitsHTMLGenerator();
//	String html = generator.generateHTML(clusters, labeledSOM);
//	try {
//	    FileWriter fw = new FileWriter(new File("SOM_KMeans.html"));
//	    fw.write(html);
//	    fw.close();
//	} catch (IOException e1) {
//	    e1.printStackTrace();
//	}
//
//    }

}
