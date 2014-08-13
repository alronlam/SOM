import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import kohonen.LearningData;
import kohonen.WTMLearningFunction;
import learningFactorFunctional.GaussFunctionalFactor;
import learningFactorFunctional.LearningFactorFunctionalModel;
import metrics.EuclidesMetric;
import network.DefaultNetwork;
import network.NetworkModel;
import network.NeuronModel;
import topology.GaussNeighbourhoodFunction;
import topology.MatrixTopology;
import topology.TopologyModel;


public class SOMFacade {

	private int numRows;
	private int numCols;
	private int numIterations;
	private int startingRadius;
	private double learningRate;
	
	public SOMFacade(){
		this.numRows = 15;
		this.numCols = 15;
		this.numIterations = 15;
		this.learningRate = 0.5;
	}
	
	public void setRows(int rows){
		this.numRows = rows;
		this.recalculateRadius();
	}
	
	public void setCols(int cols){
		this.numCols = cols;
		this.recalculateRadius();
	}
	
	public void setNumIterations(int iterations){
		this.numIterations = iterations;
	}
	
	public void setLearningRate(int rate){
		this.learningRate = rate;
	}
	
	private void recalculateRadius(){
		this.startingRadius = Math.max(numRows, numCols)/2;
	}
	
	
	//Remember to call the setters if you don't want the default parameters
	public void testSOM(int numAttributes, double[] maxWeights, String inputFileWithoutClass, String inputFileWithClass, String outputFileName){
		
		TopologyModel topology = new MatrixTopology(this.numRows, this.numCols, startingRadius);
		DefaultNetwork network = new DefaultNetwork(numAttributes, maxWeights, topology);
		
		LearningFactorFunctionalModel learningFactor = new GaussFunctionalFactor(learningRate);
//		LearningFactorFunctionalModel learningFactor =new ExponentionalFunctionFactor(learningRate, decay);
		GaussNeighbourhoodFunction gaussNeighborhood = new GaussNeighbourhoodFunction(startingRadius);
		
		LearningData datasetWOClass = new LearningData(inputFileWithoutClass);
		WTMLearningFunction learning = new WTMLearningFunction(network,numIterations,new EuclidesMetric(),datasetWOClass, learningFactor, gaussNeighborhood);
		learning.setShowComments(true);
		learning.learn();
		network.networkToFile(outputFileName);
		
		LearningData datasetWClass = new LearningData("data/dataset.csv");
		LinkedList<LinkedList<String>> classNetwork = convertToClasses(network, datasetWClass);
		
		try{
		
			FileWriter fw = new FileWriter(new File(outputFileName), true);
			String stringForm = convertToString(classNetwork);
		
			System.out.print(stringForm);
			fw.append(stringForm);
			fw.close();
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	private String convertToString(LinkedList<LinkedList<String>> network){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<network.size(); i++){
			LinkedList<String> currRow = network.get(i);
			for(int j=0;j<currRow.size(); j++){
				if(j > 0)
					sb.append(" ");
				sb.append(currRow.get(j));
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	private LinkedList<LinkedList<String>> convertToClasses(NetworkModel network, LearningData dataset){
		int nRows = network.getTopology().getRowNumber();
		int nCols = network.getTopology().getColNumber();
		
		LinkedList<LinkedList<String>> classNetwork = new LinkedList<LinkedList<String>>();
		
		for(int i=0; i<nRows; i++){
			
			LinkedList<String> row = new LinkedList<String>();
			
			for(int j=0; j<nCols; j++){
				NeuronModel currNeuron = network.getNeuron(i*nCols+j);
				int nearestClass = getClassOfNearestInstance(dataset, currNeuron.getWeight());
				row.add(nearestClass+"");
			}
			
			classNetwork.add(row);
		}
		
		return classNetwork;
	}
	
	private int getClassOfNearestInstance(LearningData dataset, double[] testInstanceWithoutClass){
		if(dataset.getDataSize() == 0)
			return -1;
		
		int nearestIndex = 0;
		double currNearestDistance = calculateEuclideanDistance(removeLastIndex(dataset.getData(0)), testInstanceWithoutClass);
		
		for(int i=1; i<dataset.getDataSize(); i++){
			double[] currInstanceWithoutClass = removeLastIndex(dataset.getData(i));
			double currEuclideanDistance = calculateEuclideanDistance(currInstanceWithoutClass, testInstanceWithoutClass);

			if(currEuclideanDistance < currNearestDistance ){
				currNearestDistance = currEuclideanDistance;
				nearestIndex = i;
			}
		}
		
		//return nearestIndex;
		
//		System.out.println("-------------------");
//		System.out.println(convertToString(testInstanceWithoutClass));
//		System.out.println("is nearest to");
//		System.out.println(convertToString(dataset.getData(nearestIndex)));
//		System.out.println("with a distance of "+currNearestDistance);
//		System.out.println("-------------------");
		
		int nearestClass = (int) Math.round( dataset.getData(nearestIndex)[dataset.getVectorSize()-1] );
		
		return nearestClass;
		
	}
	
	private String convertToString(double[] array){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i <array.length; i++)
			sb.append(array[i]+" ");
		return sb.toString();
	}
	
	private double[] removeLastIndex(double[] array){
		double[] newArray = new double[array.length-1];
		for(int i=0;i<array.length-1;i++){
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	private double calculateEuclideanDistance(double[] instance1, double[] instance2){
		if(instance1.length != instance2.length){
			return -1;
		}
		double sumOfSquares = 0;
		int length = instance1.length;
		
		for(int i=0; i<length; i++){
			sumOfSquares += Math.pow(instance1[i] - instance2[i], 2);
		}
		
		return Math.sqrt(sumOfSquares);
	}
	
	
	
}
