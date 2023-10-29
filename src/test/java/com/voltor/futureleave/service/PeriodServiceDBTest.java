package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.voltor.futureleave.builder.PeriodBuilder;
import com.voltor.futureleave.model.Period;

class PeriodServiceDBTest extends AbstractServiceDBTest{
	
	@Autowired private PeriodService periodService;
	@Autowired private PeriodBuilder builder;
	
	@Test
	void simpleCreateTest() {
		Period period = builder.buildNew(); 
		Period result =  periodService.create(period);
		assertNotNull(result.getId());
		assertNotEquals( 0L, result.getId());
	}

}
