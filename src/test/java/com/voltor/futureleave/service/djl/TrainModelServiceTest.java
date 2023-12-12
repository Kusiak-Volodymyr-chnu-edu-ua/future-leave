package com.voltor.futureleave.service.djl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import com.voltor.futureleave.model.Period;
import com.voltor.futureleave.service.ai.Trainer;

class TrainModelServiceTest {

	Trainer trainModelService = new Trainer();

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Test
	void learnModelSimpleTest() throws Exception {
		trainModelService.learnBase();
		
		Period s = new Period();
		Integer total = 124;
		Integer unplanned = 105;

		s.setPlannedEventsCount(total - unplanned);
		s.setUnplannedEventsCount(unplanned);
		s.setStartDate(LocalDateTime.parse("2023-08-21 00:00:00", formatter).toLocalDate());
		s.setEndDate(LocalDateTime.parse("2023-09-18 00:00:00", formatter).toLocalDate()); 
		
		System.out.println( trainModelService.getPredict(s) ); 
	}

}
