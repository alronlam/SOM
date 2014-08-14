import htmlgenerator.DigitsHTMLGenerator;
import htmlgenerator.HTMLGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import converters.DigitsNeuronsToInstancesConverter;
import converters.NeuronsToInstancesConverter;
import som.SOMFacade;
import som.labeler.NearestNeighborLabeler;
import som.labeler.SOMNetworkLabeler;
import som.network.DefaultNetwork;
import datasets.DatasetManager;
import datasets.DatasetManagerFactory;
import weka.core.Instances;

public class TestDriver {

    public static void main(String[] args) {
	runSOMOnOurDataset();
    }

    private static void runSOMOnOurDataset() {

	// Prepare the dataset
	DatasetManager digitsDM = DatasetManagerFactory
		.create(DatasetManagerFactory.DIGITS);

	// Run SOM
	SOMFacade som = new SOMFacade();
	som.setNumIterations(10);

	int numAttributes = 64;
	double[] maxWeights = new double[numAttributes];
	for (int i = 0; i < numAttributes; i++)
	    maxWeights[i] = 16;

	DefaultNetwork network = som.runSOM(numAttributes, maxWeights,
		digitsDM.getDataWithoutClass());

	// Convert the SOM network to Instances (Weka class)
	NeuronsToInstancesConverter digitsConverter = new DigitsNeuronsToInstancesConverter();
	Instances instances = digitsConverter.convert(network, "Digits");

	// Label the nodes in the network
	SOMNetworkLabeler somLabeler = new NearestNeighborLabeler();
	LinkedList<LinkedList<String>> labeledSOM = somLabeler.labelNetwork(
		network, digitsDM);

	// Run K-Means
	KMeansFacade kMeans = new KMeansFacade();
	int[] clusters = kMeans.runKMeans(network, 10, instances);

	// Genrate HTML File
	HTMLGenerator generator = new DigitsHTMLGenerator();
	String html = generator.generateHTML(clusters, labeledSOM);
	try {
	    FileWriter fw = new FileWriter(new File("SOM_KMeans.html"));
	    fw.write(html);
	    fw.close();
	} catch (IOException e1) {
	    e1.printStackTrace();
	}

    }

}
