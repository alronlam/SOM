package helper;

import java.util.LinkedList;

public class Helper {

    public static double calculateEuclideanDistance(double[] instance1,
	    double[] instance2) {
	if (instance1.length != instance2.length) {
	    return -1;
	}
	double sumOfSquares = 0;
	int length = instance1.length;

	for (int i = 0; i < length; i++) {
	    sumOfSquares += Math.pow(instance1[i] - instance2[i], 2);
	}

	return Math.sqrt(sumOfSquares);
    }

    public static double[] removeIndex(double[] array, int index) {
	double[] newArray = new double[array.length - 1];
	for (int i = 0, j = 0; i < array.length; i++) {
	    if (index != i)
		newArray[j++] = array[i];
	}
	return newArray;
    }

    public static String convertToString(double[] array) {
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < array.length; i++)
	    sb.append(array[i] + " ");
	return sb.toString();
    }

    public static String convertToString(LinkedList<LinkedList<String>> network) {
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < network.size(); i++) {
	    LinkedList<String> currRow = network.get(i);
	    for (int j = 0; j < currRow.size(); j++) {
		if (j > 0)
		    sb.append(" ");
		sb.append(currRow.get(j));
	    }
	    sb.append("\n");
	}

	return sb.toString();
    }

    public static LinkedList<Double> insertInOrder(LinkedList<Double> list, double val) {
	LinkedList<Double> newList = new LinkedList<Double>(list);

	int index = 0;
	for (int i = 0; i < newList.size(); i++) {
	    if (val < newList.get(i)) {
		index = i;
		break;
	    }
	}

	newList.add(index, val);

	return newList;
    }
}
