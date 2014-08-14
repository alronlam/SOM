import som.network.DefaultNetwork;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class KMeansFacade {

    public int[] runKMeans(DefaultNetwork network, int numClusters,
	    Instances instances) {
	SimpleKMeans kmeans = new SimpleKMeans();

	kmeans.setSeed(10);

	// This is the important parameter to set
	kmeans.setPreserveInstancesOrder(true);

	try {
	    kmeans.setNumClusters(numClusters);
	    kmeans.buildClusterer(instances);

	    return kmeans.getAssignments();

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return new int[0];
    }

}
