package datasets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class InternetDatasetManager extends DatasetManager {

    private LinkedList<String> classes;
    
    protected InternetDatasetManager(String filePathOriginal, String filePathWithoutClass, int classIndex) {
	super(filePathWithoutClass, filePathWithoutClass, classIndex);
	

	classes = new LinkedList<String>();
	
	try {
	    Scanner scanner = new Scanner(new File(filePathOriginal));
	    
	    while(scanner.hasNext()){
		String[] tokens = scanner.nextLine().split(",");
		classes.add(tokens[classIndex]);
	    }
	    
	    scanner.close();
	    
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	
	

    }
    
    public String getClassOf(int index) {
  	return classes.get(index);
      }


}
