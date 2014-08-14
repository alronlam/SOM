import htmlgenerator.DigitsHTMLGenerator;
import htmlgenerator.HTMLGenerator;

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
import converters.NeuronsToInstancesConverter;
import datasets.DatasetManager;
import datasets.DatasetManagerFactory;

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
	som.setRows(20);
	som.setCols(20);
	som.setNumIterations(10);

	int numAttributes = digitsDM.getNumAttributesWithoutClass();
	double[] maxWeights = new double[numAttributes];
	for (int i = 0; i < numAttributes; i++)
	    maxWeights[i] = 16;

	DefaultNetwork network = som.runSOM(numAttributes, maxWeights,
		digitsDM.getDataWithoutClass());

	// Convert the SOM network to Instances (Weka class)
	NeuronsToInstancesConverter digitsConverter = new DigitsNeuronsToInstancesConverter();
	Instances instances = digitsConverter.convert(network, "Digits");

	// Label the nodes in the network
	SOMNetworkLabeler somLabeler = new NearestNeighborLabeler(8);
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
