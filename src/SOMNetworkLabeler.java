
import java.util.LinkedList;

import network.DefaultNetwork;

public interface SOMNetworkLabeler {

    public LinkedList<LinkedList<String>> labelNetwork(DefaultNetwork network,
	    DatasetManager datasetManager);

    public String labelNeuron(double[] weights, DatasetManager datasetManager);
}
