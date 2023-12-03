package com.voltor.futureleave.service.djl;

import java.io.IOException;
import java.nio.file.Paths;

import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.Mnist;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.Record;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;

public class TrainModelService {

	
	 private static final String MODEL_DIR = "./model/mnist";
	    private static final String MODEL_NAME = "mlp";
 
	    public void train(String modelDir) throws Exception {
	        // Construct neural network
	        Block block = new Mlp( 3, 1, new int[]{24, 12});

	        try (Model model = Model.newInstance(MODEL_NAME)) {
	            model.setBlock(block);

	            // get training and validation dataset
	            RandomAccessDataset trainingSet = prepareDataset(Dataset.Usage.TRAIN);
	            RandomAccessDataset validateSet = prepareDataset(Dataset.Usage.TEST);

	            // setup training configuration
	            DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
	                    .addEvaluator(new Accuracy())
	                    .addTrainingListeners(TrainingListener.Defaults.logging());

	            try (Trainer trainer = model.newTrainer(config)) {
	                trainer.setMetrics(new Metrics());

	                Shape inputShape = new Shape(1, Mnist.IMAGE_HEIGHT * Mnist.IMAGE_WIDTH);

	                // initialize trainer with proper input shape
	                trainer.initialize(inputShape);
	                EasyTrain.fit(trainer, 10, trainingSet, validateSet);
	            }
	            model.save(Paths.get(modelDir), MODEL_NAME);
	        }
	    }

	    private RandomAccessDataset prepareDataset(Dataset.Usage usage) throws IOException {
//	        Mnist mnist = Mnist.builder().optUsage(usage).setSampling(batchSize, true).optLimit(limit).build();
//	        mnist.prepare(new ProgressBar());
//	    	ArrayDataset.Builder builder = new ArrayDataset.Builder();
//	    	NDArray ss =  new NDArray
//	    	builder.setData(null)
//	        return new ArrayDataset(builder);
	    	return null;
	    }
}
