package htmlgenerator;
import java.util.LinkedList;

public class InternetHTMLGenerator implements HTMLGenerator {

    public String generateHTML(int[] clusters,
	    LinkedList<LinkedList<String>> labeledSOM) {

	StringBuilder sb = new StringBuilder();
	sb.append("<html> \n <title></title> <body><p>");
	sb.append(createColoredString(clusters, labeledSOM));
	sb.append("\n </p></body> </html>");

	return sb.toString();
    }

    private static String createColoredString(int[] clusters,
	    LinkedList<LinkedList<String>> labeledSOM) {

	int numRows = labeledSOM.size();
	int numCols = labeledSOM.get(0).size();
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < numRows; i++) {

	    LinkedList<String> currRow = labeledSOM.get(i);

	    for (int j = 0; j < numCols; j++) {

		String category = currRow.get(j);

		int currNeuronIndex = i * numCols + j;
		int clusterNum = clusters[currNeuronIndex];

		sb.append("<span style='color:");

		switch (clusterNum) {
		case 0:
		    sb.append("red");
		    break;
		case 1:
		    sb.append("orange");
		    break;
		case 2:
		    sb.append("yellow");
		    break;
		case 3:
		    sb.append("green");
		    break;
		case 4:
		    sb.append("blue");
		    break;
		case 5:
		    sb.append("violet");
		    break;
		case 6:
		    sb.append("brown");
		    break;
		case 7:
		    sb.append("gray");
		    break;
		case 8:
		    sb.append("black");
		    break;
		case 9:
		    sb.append("cyan");
		    break;
		}

		sb.append("'>");
		sb.append(category);
		sb.append("</span> ");
	    }

	    sb.append("<br>");
	}
	return sb.toString();
    }

}
