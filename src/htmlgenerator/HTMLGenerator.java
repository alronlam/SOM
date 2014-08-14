package htmlgenerator;
import java.util.LinkedList;

public interface HTMLGenerator {

    public String generateHTML(int[] clusters,  LinkedList<LinkedList<String>> labeledSOM);
}
