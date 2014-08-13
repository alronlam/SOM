import java.util.LinkedList;

import kohonen.LearningData;
import kohonen.WTMLearningFunction;
import learningFactorFunctional.ExponentionalFunctionFactor;
import learningFactorFunctional.GaussFunctionalFactor;
import learningFactorFunctional.LearningFactorFunctionalModel;
import metrics.EuclidesMetric;
import network.DefaultNetwork;
import network.NetworkModel;
import network.NeuronModel;
import topology.GaussNeighbourhoodFunction;
import topology.MatrixTopology;
import topology.TopologyModel;


public class TestDriver {

	public static void main(String[] args){
		
	}
	
	private void runSOMOnOurDataset(){
		SOMFacade som = new SOMFacade();
		
		int numAttributes = 64;
		double[] maxWeights = new double[numAttributes];
		for(int i=0;i<numAttributes; i++)
			maxWeights[i] = 16;
		
		som.testSOM(numAttributes,maxWeights,"data/dataset_wo_class.csv", "data/dataset.csv", "OursOutput.txt" );
	}
	
}
