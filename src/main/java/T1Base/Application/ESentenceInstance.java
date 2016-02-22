package T1Base.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.nd4j.linalg.api.ndarray.BaseNDArray;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.NDArray;

import T1Base.Learner.SentenceFeatures;
import T1Base.Learner.StringFeatures;
import T1Base.Parser.EntityType;
import T1Base.Parser.POS;
import T1Base.Parser.POSTag;
import T1Base.Parser.Sentence;
import gerkosoft.MLEvaluation.Interfaces.Instance;

public class ESentenceInstance implements Instance<EntityType> {
	private Sentence sentence;
	private int index;
	public static final int LABEL_VECTOR_LENGTH=4;
	public ESentenceInstance(Sentence sentence, int index){
		this.sentence=sentence;
		this.index=index;
	}

	public EntityType getLabel() {
		return getPart().getEntityType();
	}
	
	public INDArray getFeatures(){
		StringFeatures featureExtractor=new StringFeatures(this.getPart().getPhrase(), this.index);
		SentenceFeatures f=new SentenceFeatures(this);
		f
		.AddFeatures(-1, "in", "at", "on", "the", "a", "an", "is", "are", "am", "was", "were", "will", "have", "has", "'ve", "'m", "'s")
		.AddFeatures(-2, "in", "at", "on", "the", "a", "an", "is", "are", "am", "was", "were", "will", "have", "has", "'ve", "'m", "'s")
		.AddFeatures(+1, "is", "are", "am", "was", "were", "will", "have", "has", "'ve", "'m", "'s")
		.AddFeatures(+2, "is", "are", "am", "was", "were", "will", "have", "has")
		.AddFeatures(featureExtractor.countCapitals(), featureExtractor.firstCapital(), featureExtractor.numLetters())
		.AddFeatures(featureExtractor.numNumbers(), featureExtractor.numWords(), featureExtractor.countPunctuation())
		.AddHasPOSTagStarting(+1, "vb")
		.AddHasPOSTagStarting(-1, "jj")
		.AddInRangeFeature(featureExtractor.numLetters(),0,3)
		.AddInRangeFeature(featureExtractor.numLetters(),4,6)
		.AddInRangeFeature(featureExtractor.numLetters(),7,Float.MAX_VALUE)
		.AddFeatures(this.getPart().getPOSTags());
		return f.toArray();
	}
	
	public INDArray getNNLabels(){
		float[] labels=new float[LABEL_VECTOR_LENGTH];
		labels[getPart().getEntityType().getID()-1]=1.0f;
		return new NDArray(labels);
	}
	
	public POS getPart(){
		return this.sentence.getPart(this.index);
	}
	public POS getPart(int index){
		return this.sentence.getPart(index);
	}
	
	public int size(){
		return this.sentence.size();
	}
	
	public int getIndex(){
		return this.index;
	}
	
	@Override
	public String toString(){
		return this.toString()+"\n"+this.getPart().getPhrase().toString();
	}
}
