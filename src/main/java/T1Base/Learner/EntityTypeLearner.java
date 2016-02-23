package T1Base.Learner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.NDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import T1Base.Application.ESentenceInstance;
import T1Base.Parser.EntityType;
import T1Base.Parser.SentencePropertyMapper;
import gerkosoft.MLEvaluation.Interfaces.Learner;

public class EntityTypeLearner implements Learner<ESentenceInstance, EntityType> {
	private static final int LABEL_VECTOR_LENGTH=4;
	private MultiLayerNetwork network;
	private SentencePropertyMapper mapper;
	public EntityTypeLearner(SentencePropertyMapper mapper){
		this.mapper=mapper;
	}
	public void learn(Collection<ESentenceInstance> trainingData) {
		int middleLayerSize=10;
		MultiLayerConfiguration config=new NeuralNetConfiguration.Builder()
                .seed(12345)
                .iterations(30)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(1e-1)
                .l2(0.001)
                .list(2)
                .layer(0, new DenseLayer.Builder().nIn(getFeatures(trainingData.iterator().next()).length()).nOut(middleLayerSize)
                        .weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAGRAD)
                        .activation("relu").build())
                .layer(1, new OutputLayer.Builder().nIn(middleLayerSize).nOut(LABEL_VECTOR_LENGTH)
                        .weightInit(WeightInit.XAVIER)
                        .updater(Updater.ADAGRAD)
                        .activation("softmax").lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .build())
                .pretrain(false).backprop(true)
                .build();
		network=new MultiLayerNetwork(config);
		List<INDArray> inputs=this.Batch(new ArrayList<ESentenceInstance>(trainingData), 1000);
		List<INDArray> labels=this.BatchLabels(new ArrayList<ESentenceInstance>(trainingData), 1000);
		/*for(ESentenceInstance i : trainingData){
			network.fit(getFeatures(i), getLabel(i));
		}*/
		for(int times=0;times<50;times++){
			for(int i=0;i<inputs.size();i++){
				network.fit(inputs.get(i), labels.get(i));
			}
		}
	}

	public EntityType predict(ESentenceInstance input) {
		INDArray output=network.output(getFeatures(input));
		EntityType t=null;
		float max=0;
		for(int i=0;i<LABEL_VECTOR_LENGTH;i++){
			if(output.getFloat(i)>max){
				t=mapper.getEntity(i+1);
				max=output.getFloat(i);
			}
		}
		return t;
	}
	
	private INDArray getFeatures(ESentenceInstance instance){
		StringFeatures featureExtractor=new StringFeatures(instance.getPart().getPhrase(), instance.getIndex());
		return getSentenceFeatures(instance).toNDArray();
	}
	
	private SentenceFeatures getSentenceFeatures(ESentenceInstance instance){
		StringFeatures featureExtractor=new StringFeatures(instance.getPart().getPhrase(), instance.getIndex());
		return new SentenceFeatures(instance)
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
		.AddFeatures(instance.getPart().getPOSTags())
		.AddFeatures(instance.getPart().getEntityType().getID()==1)
		.AddFeatures(instance.getPart().getEntityType().getID()==2)
		.AddFeatures(instance.getPart().getEntityType().getID()==3)
		.AddFeatures(instance.getPart().getEntityType().getID()==4)
		.AddFeatures(instance.getPart().getEntityType().getID()==5)
		.AddFeatures(1.0f);
	}
	
	private List<INDArray> Batch(List<ESentenceInstance> features, int batchSize){
		List<INDArray> arrays=new ArrayList<INDArray>();
		for(int i=0;i<features.size();i+=batchSize){
			int size=batchSize;
			if(i+size > features.size()){
				size=features.size()-i;
			}
			float[][] matrix=new float[size][];
			for(int j=0;j<size;j++){
				matrix[j]=getSentenceFeatures(features.get(i+j)).toArray();
			}
			arrays.add(new NDArray(matrix));
		}
		return arrays;
	}
	private List<INDArray> BatchLabels(List<ESentenceInstance> features, int batchSize){
		List<INDArray> arrays=new ArrayList<INDArray>();
		for(int i=0;i<features.size();i+=batchSize){
			int size=batchSize;
			if(i+size > features.size()){
				size=features.size()-i;
			}
			float[][] matrix=new float[size][];
			for(int j=0;j<size;j++){
				matrix[j]=getSentenceLabels(features.get(i+j));
			}
			arrays.add(new NDArray(matrix));
		}
		return arrays;
	}

	private INDArray getLabel(ESentenceInstance instance){
		return new NDArray(getSentenceLabels(instance));
	}

	private float[] getSentenceLabels(ESentenceInstance instance){
		float[] labels=new float[LABEL_VECTOR_LENGTH];
		labels[instance.getPart().getEntityType().getID()-1]=1.0f;
		return labels;
	}
}
