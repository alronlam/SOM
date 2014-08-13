import java.io.File;

import kohonen.LearningData;
import kohonen.WTMLearningFunction;


public class DigitDatasetManager {

	private static DigitDatasetManager instance;
	
	public static DigitDatasetManager getInstance(){
		if(instance == null)
			instance = new DigitDatasetManager();
		return instance;
	}
	
	private static final String DATA_ORIGINAL = "data/dataset.csv";
	private static final String DATA_WO_CLASSES = "data/dataset_wo_class.csv";
	
	
	public File retrieveOriginalDataset(){
		return new File(DATA_ORIGINAL);
	}
	
	public File retrieveDatasetWithoutClasses(){
		return new File(DATA_WO_CLASSES);
	}
	
}
