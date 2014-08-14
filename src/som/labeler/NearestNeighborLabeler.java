package som.labeler;

import helper.Helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import som.network.DefaultNetwork;
import som.network.NeuronModel;
import datasets.DatasetManager;

public class NearestNeighborLabeler implements SOMNetworkLabeler {

    private int k;

    public NearestNeighborLabeler(int k) {
	this.k = k;
    }

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

	LinkedList<DistanceIndexPair> neighbors = new LinkedList<DistanceIndexPair>();

	for (int i = 0; i < numInstances; i++) {

	    double currEuclideanDistance = Helper.calculateEuclideanDistance(
		    datasetManager.getRowWithoutClass(i), weights);

	    if (neighbors.size() == 0
		    || currEuclideanDistance < neighbors.getLast().distance) {

		neighbors.add(new DistanceIndexPair(currEuclideanDistance, i));
		Collections.sort(neighbors);
		if (neighbors.size() > k)
		    neighbors.removeLast();

	    }

	}

	// get the majority class in the k-nearest neighbors
	HashMap<String, Double> classCount = new HashMap<String, Double>();
	for (DistanceIndexPair neighbor : neighbors) {
	    String neighborClass = "" + (int) Math.round(datasetManager.getClassOf(neighbor.index));
	    if (classCount.containsKey(neighborClass))
		classCount.put(neighborClass, classCount.get(neighborClass) + neighbor.distance);
	    else
		classCount.put(neighborClass, neighbor.distance);
	}

	double max = -1;
	String majorityCategory = "";
	for (String category : classCount.keySet()) {
	    if (classCount.get(category) > max) {
		max = classCount.get(category);
		majorityCategory = category;
	    }
	}

	return majorityCategory;
    }
    // @Override
    // public String labelNeuron(double[] weights, DatasetManager
    // datasetManager) {
    //
    // int numInstances = datasetManager.getDataSize();
    //
    // if (numInstances == 0)
    // return "";
    //
    // int nearestIndex = 0;
    // double currNearestDistance = Helper.calculateEuclideanDistance(
    // datasetManager.getRowWithoutClass(0), weights);
    //
    // for (int i = 1; i < numInstances; i++) {
    //
    // double currEuclideanDistance = Helper.calculateEuclideanDistance(
    // datasetManager.getRowWithoutClass(i), weights);
    //
    // if (currEuclideanDistance < currNearestDistance) {
    // currNearestDistance = currEuclideanDistance;
    // nearestIndex = i;
    // }
    // }
    //
    // int nearestClass = (int) Math.round(datasetManager
    // .getClassOf(nearestIndex));
    //
    // // System.out.println("-------------------");
    // // System.out.println(Helper.convertToString(weights));
    // // System.out.println("is nearest to");
    // // System.out.println(Helper.convertToString(datasetManager
    // // .getRowWithoutClass(nearestIndex)));
    // // System.out.println("with a distance of " + currNearestDistance);
    // // System.out.println("nearest class is " + nearestClass);
    // // System.out.println("-------------------");
    //
    // return "" + nearestClass;
    // }
}

class DistanceIndexPair implements Comparable<DistanceIndexPair> {

    public double distance;
    public int index;

    public DistanceIndexPair(double distance, int index) {
	this.distance = distance;
	this.index = index;
    }

    @Override
    public int compareTo(DistanceIndexPair o) {
	double diff = this.distance - o.distance;
	if (diff > 0)
	    return 1;
	if (diff < 0)
	    return -1;
	return 0;
    }

}