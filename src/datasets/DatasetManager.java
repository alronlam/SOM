package datasets;

import som.kohonen.LearningData;

public class DatasetManager {

    private LearningData dataOriginal;
    private LearningData dataWithoutClass;
    private int classIndex;

    protected DatasetManager(String filePathOriginal,
	    String filePathWithoutClass, int classIndex) {
	this.dataOriginal = new LearningData(filePathOriginal);
	this.dataWithoutClass = new LearningData(filePathWithoutClass);
	this.classIndex = classIndex;
    }

    public LearningData getDataOriginal() {
	return dataOriginal;
    }

    public LearningData getDataWithoutClass() {
	return dataWithoutClass;
    }

    public int getNumAttributesOriginal() {
	return dataOriginal.getVectorSize();
    }

    public int getNumAttributesWithoutClass() {
	return dataWithoutClass.getVectorSize();
    }

    public double[] getRow(int index) {
	return dataOriginal.getData(index);
    }

    public double[] getRowWithoutClass(int index) {
	return dataWithoutClass.getData(index);
    }

    public int getDataSize() {
	return dataOriginal.getDataSize();
    }

    public double getClassOf(int index) {
	return dataOriginal.getData(index)[classIndex];
    }

}
