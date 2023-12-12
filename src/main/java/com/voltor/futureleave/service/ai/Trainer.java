package com.voltor.futureleave.service.ai;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.voltor.futureleave.model.Period;
 
@Component
public class Trainer {

	private static Logger log = LoggerFactory.getLogger(Trainer.class);
	private final static String MODEL_PATH = "model.json";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static double MIN_DATE_NORM = Trainer.buildBase(LocalDate.of(2015, 1, 1));
	private double MAX_VALUE =  100;
	private int NUM_OUTPUTS = 10;
	private int PART = (int) (MAX_VALUE / NUM_OUTPUTS);
 
	public void learnBase() throws Exception {
		String csvFilePath = "data-y-1w.csv";
		URL resource = getClass().getClassLoader().getResource(csvFilePath);

		try (CSVReader reader = new CSVReader(new FileReader(resource.getFile()))) {
			// Read all rows at once
			List<String[]> rows = reader.readAll();

			// Convert each row to a Person object
			List<Period> persons = mapRowsToPersons(rows);
			learn(persons);

		}
	}

	private List<Period> mapRowsToPersons(List<String[]> rows) {
		// Assuming the CSV structure is: name,age,email
		// You may need to adapt this part based on your CSV structure
		return rows.stream().skip(1) // Skip the header row if it exists
				.map(row -> {
					Period s = new Period();
					Integer total = Integer.valueOf(row[0]);
					Integer unplanned = Integer.valueOf(row[1]);

					s.setPlannedEventsCount(total - unplanned);
					s.setUnplannedEventsCount(unplanned);
					s.setStartDate(LocalDateTime.parse(row[2], formatter).toLocalDate());
					s.setEndDate(LocalDateTime.parse(row[3], formatter).toLocalDate());
					return s;
				}).toList();
	}
	 
	public void learn(List<Period> dataToLearn) throws Exception {

		List<INDArray> inputList = new ArrayList<>();
		List<INDArray> outputList = new ArrayList<>();

		for (Period period : dataToLearn) { 
			inputList.add( periodToLearn(period) );
			outputList.add( Nd4j.create( toBinary( period.getUnplannedEventsCount() ) ) );
		}
		 
		// Задаємо параметри мережі
        int numInput = 3; // Кількість вхідних значень
        int numOutput = NUM_OUTPUTS; // Кількість вихідних значень
        int numHidden = 15; // Кількість прихованих нейронів
        
        
        INDArray input = Nd4j.vstack(inputList.toArray(new INDArray[0]));
        INDArray labels = Nd4j.vstack(outputList.toArray(new INDArray[0]));
 
		System.out.println("Build model....");

		// Fit the model
		MultiLayerNetwork model = null;
		
		boolean exists = isNetwork();
		if(exists) {
			model = loadMultiLayerNetwork();
		} else {
			model = createConfig( numInput, numOutput, numHidden);
		}
		
		model.init();
		model.setListeners(new ScoreIterationListener(100));

		SplitTestAndTrain testAndTrain = new org.nd4j.linalg.dataset.DataSet(input, labels).splitTestAndTrain(0.65);
		 
		DataSet trainingData = testAndTrain.getTrain();
		DataSet testData = testAndTrain.getTest();
		
		 DataNormalization normalizer = new NormalizerStandardize();
	        normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
	        normalizer.transform(trainingData);     //Apply normalization to the training data
	        normalizer.transform(testData);     

 
		
		 for (int i = 0; i < 10000; i++) {
	            model.fit(trainingData.getFeatures(), trainingData.getLabels());
	        }
		ModelSerializer.writeModel(model, MODEL_PATH, true);

		// Evaluate the model on the test set
		Evaluation eval = new Evaluation();
		INDArray output = model.output(testData.getFeatures());

		eval.eval(testData.getLabels(), output);
		log.info(eval.stats());


	}

	public boolean isNetwork() {
		return new File(MODEL_PATH).exists();
	}
 
	public MultiLayerNetwork createConfig(int numInputs, int numOutputs, int numHiddenUnits) { 

	    // Створюємо конфігурацію мережі
	    NeuralNetConfiguration.ListBuilder builder = new NeuralNetConfiguration.Builder()
	            .seed(123)
                .updater(new org.nd4j.linalg.learning.config.Adam(0.001))
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(numInputs*2)
                    .build())
                .layer(new DenseLayer.Builder().nIn(numInputs*2).nOut(numOutputs*2)
                		.activation(Activation.CUBE)
                    .build())
                .layer(new DenseLayer.Builder().nIn(numOutputs*2).nOut(numOutputs*2)
                        .build())
                .layer( new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .activation(Activation.SOFTMAX) //Override the global TANH activation with softmax for this layer
                    .nIn(numOutputs*2).nOut(numOutputs).build());

	    return new MultiLayerNetwork(builder.build());
    }

	
	private MultiLayerNetwork loadMultiLayerNetwork() {
		try {
			return ModelSerializer.restoreMultiLayerNetwork(MODEL_PATH, true);
		}  catch (IOException e) {
			log.error(" getForecast error", e);
		}
		return null;
	}

	public int getPredict( Period period ) {
		MultiLayerNetwork layerNetwork = loadMultiLayerNetwork();
		if(layerNetwork==null) {
			return -1;
		}
		
		INDArray input = periodToLearn(period);
 
 
		INDArray inputMatrix = input.reshape(1, input.length());
		
		int[] result = layerNetwork.predict( inputMatrix );
		return result[0]*PART;
	}
	
	private static double getValueFromDate( LocalDate date ) {
		 
		return  (double) buildBase(date) - Trainer.MIN_DATE_NORM;
	}
	
	private static double buildBase( LocalDate date ) {
		
		Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
		return  (double) ( (instant.toEpochMilli() / ( 1000 * 60 * 60 * 24 ) )-10000);
	}
	
	public INDArray periodToLearn( Period period ) {
		return Nd4j.create(  new double[] { period.getPlannedEventsCount() , 
				  getValueFromDate( period.getStartDate() ), 
					  getValueFromDate( period.getEndDate() )  } );
	}
	
	public void removeNetwork() {
		File file = new File(MODEL_PATH);
        if( file.delete() ) { 
            System.out.println("Network deleted successfully"); 
        } 
	}
	
	public double[] toBinary( int num ) {
		double[] result = new double[ NUM_OUTPUTS ];// { num/MAX_VALUE};
		int index = num/PART;
		index = index >= NUM_OUTPUTS ? (int) NUM_OUTPUTS - 1 : index;
		result [index] = 1;
		 
		return result;
		
	}

}
