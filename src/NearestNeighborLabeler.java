
import java.util.LinkedList;

import network.DefaultNetwork;
import network.NeuronModel;

public class NearestNeighborLabeler implements SOMNetworkLabeler {

    @Override
    public LinkedList<LinkedList<String>> labelNetwork(DefaultNetwork network,
	    DatasetManager datasetManager) {

	int nRows = network.getTopology().getRowNumber();
	int nCols = network.getTopology().getColNumber();

	LinkedList<LinkedList<String>> classNetwork = new LinkedList<LinkedList<String>>();

	for (int i = 0; i < nRows; i++) {

	    LinkedList<String> row = new LinkedList<String>();

	    for (int j = 0; j < nCols; j++) {
		NeuronModel currNeuron = network.getNeuron(i * nCols + j);
		String nearestClass = labelNeuron(currNeuron.getWeight(),
			datasetManager);
		row.add(nearestClass);
	    }

	    classNetwork.add(row);
	}

	return classNetwork;
    }

    @Override
    public String labelNeuron(double[] weights, DatasetManager datasetManager) {

	int numInstances = datasetManager.getDataSize();

	if (numInstances == 0)
	    return "";

	int nearestIndex = 0;
	double currNearestDistance = Helper.calculateEuclideanDistance(
		datasetManager.getRowWithoutClass(0), weights);

	for (int i = 1; i < numInstances; i++) {

	    double currEuclideanDistance = Helper.calculateEuclideanDistance(
		    datasetManager.getRowWithoutClass(i), weights);

	    if (currEuclideanDistance < currNearestDistance) {
		currNearestDistance = currEuclideanDistance;
		nearestIndex = i;
	    }
	}

	int nearestClass = (int) Math.round(datasetManager
		.getClassOf(nearestIndex));

	// System.out.println("-------------------");
	// System.out.println(Helper.convertToString(weights));
	// System.out.println("is nearest to");
	// System.out.println(Helper.convertToString(datasetManager
	// .getRowWithoutClass(nearestIndex)));
	// System.out.println("with a distance of " + currNearestDistance);
	// System.out.println("nearest class is " + nearestClass);
	// System.out.println("-------------------");

	return "" + nearestClass;
    }
}
