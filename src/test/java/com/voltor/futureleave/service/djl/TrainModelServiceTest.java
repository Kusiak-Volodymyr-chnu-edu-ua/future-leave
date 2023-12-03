package com.voltor.futureleave.service.djl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TrainModelServiceTest {

	TrainModelService trainModelService = new TrainModelService();
	
	@Test
	@Disabled
	void learnModelSimpleTest() throws Exception {
		Arguments arguments = new Arguments(); 
		arguments.initialize();
		trainModelService.train("./model/mnist");
	}

}
