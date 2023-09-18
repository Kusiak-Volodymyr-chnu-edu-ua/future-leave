package com.voltor.futureleave.config;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

import com.voltor.futureleave.service.DateTimeService;

public class StringToLocalDateTimeConverter implements Converter< String, LocalDate > {

	@Override
	public LocalDate convert( String source ) {
		return DateTimeService.parseLocalDate( source );
	}
	
}