package T1Base.Learner;

import java.util.Collection;

import T1Base.Application.ESentenceInstance;
import T1Base.Parser.EntityType;
import gerkosoft.MLEvaluation.Interfaces.Learner;

public class BaseLineLearner implements Learner<ESentenceInstance, EntityType>{
	private EntityType type;
	
	public BaseLineLearner(EntityType type){
		this.type=type;
	}

	public void learn(Collection<ESentenceInstance> arg0) { }

	public EntityType predict(ESentenceInstance arg0) {
		return type;
	}

}
