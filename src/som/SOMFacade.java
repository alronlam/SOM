package som;


import som.kohonen.LearningData;
import som.kohonen.WTMLearningFunction;
import som.learningFactorFunctional.GaussFunctionalFactor;
import som.learningFactorFunctional.LearningFactorFunctionalModel;
import som.metrics.EuclidesMetric;
import som.network.DefaultNetwork;
import som.topology.GaussNeighbourhoodFunction;
import som.topology.MatrixTopology;
import som.topology.TopologyModel;

public class SOMFacade {

    private int numRows;
    private int numCols;
    private int numIterations;
    private int startingRadius;
    private double learningRate;

    public SOMFacade() {
	this.numRows = 15;
	this.numCols = 15;
	recalculateRadius();

	this.numIterations = 10;
	this.learningRate = 0.5;
    }

    public void setRows(int rows) {
	this.numRows = rows;
	this.recalculateRadius();
    }

    public void setCols(int cols) {
	this.numCols = cols;
	this.recalculateRadius();
    }

    public void setNumIterations(int iterations) {
	this.numIterations = iterations;
    }

    public void setLearningRate(int rate) {
	this.learningRate = rate;
    }

    private void recalculateRadius() {
	this.startingRadius = Math.max(numRows, numCols) / 2;
    }

    public DefaultNetwork runSOM(int numAttributes, double[] maxWeights,
	    LearningData datasetWithoutClass) {

	TopologyModel topology = new MatrixTopology(this.numRows, this.numCols,
		startingRadius);
	DefaultNetwork network = new DefaultNetwork(numAttributes, maxWeights,
		topology);

	LearningFactorFunctionalModel learningFactor = new GaussFunctionalFactor(
		learningRate);
	GaussNeighbourhoodFunction gaussNeighborhood = new GaussNeighbourhoodFunction(
		startingRadius);

	WTMLearningFunction learning = new WTMLearningFunction(network,
		numIterations, new EuclidesMetric(), datasetWithoutClass,
		learningFactor, gaussNeighborhood);
	learning.setShowComments(true);
	learning.learn();
	// just for logging
	network.networkToFile("SOM_Output.txt");

	return network;
    }

}
