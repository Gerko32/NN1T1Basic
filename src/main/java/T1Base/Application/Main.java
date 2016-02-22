package T1Base.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import T1Base.Learner.BaseLineLearner;
import T1Base.Learner.EntityTypeLearner;
import T1Base.Parser.ERRCorporaParser;
import T1Base.Parser.EntityType;
import T1Base.Parser.Sentence;
import T1Base.Parser.SentencePropertyMapper;
import gerkosoft.MLEvaluation.Instance.FoldEvaluator;
import gerkosoft.MLEvaluation.Instance.RandomEvenDistributor;
import gerkosoft.MLEvaluation.Interfaces.Evaluator;
import gerkosoft.MLEvaluation.Interfaces.Factory;
import gerkosoft.MLEvaluation.Interfaces.Learner;

public class Main {

	public static void main(String[] args) {
		final SentencePropertyMapper mapper=new SentencePropertyMapper();
		ERRCorporaParser parser=new ERRCorporaParser(mapper);
		try {
			//List<Sentence> sentences=parser.parse("conll04.corp");
			List<Sentence> sentences=parser.parse("all.corp");
			List<ESentenceInstance> instances=getInstances(sentences);
			System.out.println(sentences.size());
			System.out.println(instances.size());
			Evaluator<ESentenceInstance, EntityType> evaluator=new FoldEvaluator<ESentenceInstance, EntityType>(new RandomEvenDistributor<ESentenceInstance>());
			System.out.println(evaluator.evaluate(new Factory<Learner<ESentenceInstance,EntityType>>(){
				public Learner<ESentenceInstance, EntityType> generateNewInstance() {
					return new EntityTypeLearner(mapper);
				}
			}, instances, 2));
			System.out.println(mapper.getPOSTags());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<ESentenceInstance> getInstances(List<Sentence> sentences){
		List<ESentenceInstance> instances=new ArrayList<ESentenceInstance>();
		for(Sentence s : sentences){
			instances.addAll(s.getEInstances());
		}
		return instances;
	}
}
