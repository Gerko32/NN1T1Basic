package T1Base.Learner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.NDArray;

import T1Base.Application.ESentenceInstance;
import T1Base.Parser.POSTag;

public class SentenceFeatures {
	private List<Float> features;
	private ESentenceInstance instance;
	
	public SentenceFeatures(ESentenceInstance instance){
		this.instance=instance;
		this.features=new ArrayList<Float>();
	}
	
	public SentenceFeatures AddFeatures(Float... newFeatures){
		this.features.addAll(Arrays.asList(newFeatures));
		return this;
	}
	
	public SentenceFeatures AddFeatures(int indexOffset, String... phrases){
		for(String p : phrases){
			this.features.add((float) isString(this.instance.getIndex()+indexOffset, p));
		}
		return this;
	}
	
	private int isString(int index, String val){
		if(index<0) return 0;
		if(index>=this.instance.size()) return 0;
		if(this.instance.getPart(index).getPhrase().toLowerCase().equals(val)) return 1;
		return 0;
	}
	
	public SentenceFeatures AddHasPOSTagStarting(int offset, String val){
		this.features.add(hasPOSTagStarting(this.instance.getIndex()+offset, val));
		return this;
	}
	
	private float hasPOSTagStarting(int index, String val){
		if(index<0) return 0;
		if(index>=this.instance.size()) return 0;
		for(POSTag tag : this.instance.getPart(index).getPOSTags()){
			if(tag.toString().toLowerCase().indexOf(val)==0) return 1;
		}
		return 0;
	}
	
	public INDArray toArray(){
		float[] featuresArray=new float[features.size()];
		for(int i=0;i<features.size();i++){
			featuresArray[i]=features.get(i).floatValue();
		}
		return new NDArray(featuresArray);
	}

	public SentenceFeatures AddInRangeFeature(float numLetters, float min, float max) {
		this.features.add(isInRange(numLetters, min, max));
		return this;
	}
	
	private float isInRange(float value, float minValue, float maxValue){
		if(value>=minValue && value <= maxValue){
			return 1.0f;
		}
		return 0.0f;
	}
	
	public SentenceFeatures AddFeatures(Set<POSTag> posTags) {
		int i=0;
		for(POSTag tag : posTags){
			if(i==3) break;
			features.add(tag.getID());
			i++;
		}
		while(i<3){
			features.add((float)0);
			i++;
		}
		return this;
	}
}
