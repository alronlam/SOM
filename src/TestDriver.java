import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class TestDriver {

	public static void main(String[] args){
		runSOMOnOurDataset();
	}
	
	private static void runSOMOnOurDataset(){
		SOMFacade som = new SOMFacade();
//		som.setRows(5);
//		som.setCols(5);
//		som.setNumIterations(5);
		
		int numAttributes = 64;
		double[] maxWeights = new double[numAttributes];
		for(int i=0;i<numAttributes; i++)
			maxWeights[i] = 16;
		
		DefaultNetwork network = som.testSOM(numAttributes,maxWeights,"data/dataset_wo_class.csv", "data/dataset.csv", "OursOutput.txt" );
		
		Instances instances = new Instances("Dataset", createTweetAttributesForDigitsDataset() , network.getNumbersOfNeurons());

		for(int i = 0 ;i < network.getNumbersOfNeurons() ; i++){
			NeuronModel neuron = network.getNeuron(i);
			Instance instance = new Instance(64);
			
			double[] weights = neuron.getWeight();
			
			for(int j=0; j<weights.length; j++){
				instance.setValue(j, weights[j]);
			}
			
			instances.add(instance);
		}
		

		KMeansFacade kMeans = new KMeansFacade();
		int[] clusters = kMeans.runKMeans(network, 10, instances);
		try {
			transferToHtmlFile(clusters, network);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void transferToHtmlFile(int[] clusters, DefaultNetwork network) throws IOException {
		FileWriter fw = new FileWriter(new File("KMeans_results.html"));
		String resultString = createColoredString(clusters, network);
		fw.append("<html> \n <title></title> <body><p>" + resultString + "\n </p></body> </html>");
		fw.close();
	}

	private static String createColoredString(int[] clusters, DefaultNetwork network ) {
		
		int numRows = network.getTopology().getRowNumber();
		int numCols = network.getTopology().getColNumber();
		StringBuilder sb = new StringBuilder();
		
		
		for(int i=0;i<numRows;i++){
			
			for(int j=0;j<numCols;j++){
				
				int currNeuronIndex = i*numCols + j;
				NeuronModel neuron = network.getNeuron(currNeuronIndex);
				String category = ""+SOMFacade.getClassOfNearestInstance(new LearningData("data/dataset.csv"), neuron.getWeight());
				
				int clusterNum = clusters[currNeuronIndex];

				sb.append("<span style='color:");
				
				switch(clusterNum){
					case 0: sb.append("red"); break;
					case 1: sb.append("orange"); break;
					case 2: sb.append("yellow"); break;
					case 3: sb.append("green"); break;
					case 4: sb.append("blue"); break;
					case 5: sb.append("violet"); break;
					case 6: sb.append("brown"); break;
					case 7: sb.append("gray"); break;
					case 8: sb.append("black"); break;
					case 9: sb.append("cyan"); break;
				}
				
				sb.append("'>");
				sb.append(category);
				sb.append("</span> ");
			}
		
			sb.append("<br>");
		}
		return sb.toString();
	}
	
	private static FastVector createTweetAttributesForDigitsDataset(){
		  
        // Declare the feature vector
        FastVector attributes = new FastVector(64);
        for(int i=1;i<=64;i++){
        	Attribute currSegmentAttribute = new Attribute("Segment"+i);
        	attributes.addElement(currSegmentAttribute);
        }
        
        return attributes;
    }
	
}
