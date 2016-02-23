package T1Base.Application;

import T1Base.Learner.EntityTypeLearner;
import T1Base.Parser.EntityType;
import T1Base.Parser.SentencePropertyMapper;
import T1Base.Parser.Statistics;
import gerkosoft.MLEvaluation.Interfaces.Factory;
import gerkosoft.MLEvaluation.Interfaces.Learner;

public class LearnerFactory implements Factory<Learner<ESentenceInstance,EntityType>> {
	private SentencePropertyMapper mapper;
	private Statistics statistics;
	public LearnerFactory(SentencePropertyMapper mapper, Statistics statistics){
		this.mapper=mapper;
		this.statistics=statistics;
	}
	public Learner<ESentenceInstance, EntityType> generateNewInstance() {
		return new EntityTypeLearner(mapper, statistics);
	}

}
