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
	public ESentenceInstance(Sentence sentence, int index){
		this.sentence=sentence;
		this.index=index;
	}

	public EntityType getLabel() {
		return getPart().getEntityType();
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
