package converters;

import som.network.DefaultNetwork;
import som.network.NeuronModel;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class NeuronsToInstancesConverter {

    public Instances convert(DefaultNetwork network, String datasetName) {

	Instances instances = new Instances(datasetName,
		createTweetAttributes(), network.getNumbersOfNeurons());
	int numAttributes = getNumAttributes();

	for (int i = 0; i < network.getNumbersOfNeurons(); i++) {
	    NeuronModel neuron = network.getNeuron(i);
	    Instance instance = new Instance(numAttributes);

	    double[] weights = neuron.getWeight();

	    for (int j = 0; j < weights.length; j++) {
		instance.setValue(j, weights[j]);
	    }

	    instances.add(instance);
	}

	return instances;
    }

    protected abstract FastVector createTweetAttributes();

    protected abstract int getNumAttributes();

}
