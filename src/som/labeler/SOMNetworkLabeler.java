package som.labeler;


import java.util.LinkedList;

import som.network.DefaultNetwork;
import datasets.DatasetManager;

public interface SOMNetworkLabeler {

    public LinkedList<LinkedList<String>> labelNetwork(DefaultNetwork network,
	    DatasetManager datasetManager);

    public String labelNeuron(double[] weights, DatasetManager datasetManager);
}
