package T1Base.Learner;

import java.util.Collection;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import T1Base.Application.ESentenceInstance;
import T1Base.Parser.EntityType;
import T1Base.Parser.SentencePropertyMapper;
import gerkosoft.MLEvaluation.Interfaces.Learner;

public class EntityTypeLearner implements Learner<ESentenceInstance, EntityType> {
	private MultiLayerNetwork network;
	private SentencePropertyMapper mapper;
	public EntityTypeLearner(SentencePropertyMapper mapper){
		this.mapper=mapper;
	}
	public void learn(Collection<ESentenceInstance> trainingData) {
		int middleLayerSize=25;
		MultiLayerConfiguration config=new NeuralNetConfiguration.Builder()
                .seed(12345)
                .iterations(30)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(1e-3)
                .l2(0.001)
                .list(2)
                .layer(0, new DenseLayer.Builder().nIn(trainingData.iterator().next().getFeatures().length()).nOut(middleLayerSize)
                        .weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAGRAD)
                        .activation("relu").build())
                .layer(1, new OutputLayer.Builder().nIn(middleLayerSize).nOut(ESentenceInstance.LABEL_VECTOR_LENGTH)
                        .weightInit(WeightInit.UNIFORM)
                        .updater(Updater.ADAGRAD)
                        .activation("relu").lossFunction(LossFunctions.LossFunction.SQUARED_LOSS)
                        .build())
                /*.list(3)
                .layer(0, new DenseLayer.Builder().nIn(ESentenceInstance.FEATURE_VECTOR_LENGTH).nOut(30)
                        .weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAGRAD)
                        .activation("relu").build())
                .layer(1, new DenseLayer.Builder().nIn(30).nOut(10)
                        .weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAGRAD)
                        .activation("relu").build())
                .layer(2, new OutputLayer.Builder().nIn(10).nOut(ESentenceInstance.LABEL_VECTOR_LENGTH)
                        .weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAGRAD)
                        .activation("relu").lossFunction(LossFunctions.LossFunction.MSE)
                        .build())*/
                .pretrain(false).backprop(true)
                .build();
		network=new MultiLayerNetwork(config);
		for(ESentenceInstance i : trainingData){
			network.fit(i.getFeatures(), i.getNNLabels());
		}
	}

	public EntityType predict(ESentenceInstance input) {
		INDArray output=network.output(input.getFeatures());
		EntityType t=null;
		float max=0;
		for(int i=0;i<ESentenceInstance.LABEL_VECTOR_LENGTH;i++){
			if(output.getFloat(i)>max){
				t=mapper.getEntity(i+1);
				max=output.getFloat(i);
			}
		}
		return t;
	}

}
