package datasets;

public class DatasetManagerFactory {

    private static final String DIGITS_DATA_ORIGINAL = "data/dataset.csv";
    private static final String DIGITS_DATA_WO_CLASSES = "data/dataset_wo_class.csv";

    private static final String INTERNET_DATA_ORIGINAL = "data/internet.csv";
    private static final String INTERNET_DATA_WO_CLASSES = "data/internet_wo_class.csv";

    
    public static final int DIGITS = 0;
    public static final int INTERNET = 1;
    
    public static DatasetManager create(int type){
	switch(type){
        	case DIGITS: return new DatasetManager(DIGITS_DATA_ORIGINAL, DIGITS_DATA_WO_CLASSES, 64);
        	case INTERNET: return new InternetDatasetManager(INTERNET_DATA_ORIGINAL, INTERNET_DATA_WO_CLASSES, 40);
	}
	return null;
    }
    
}
