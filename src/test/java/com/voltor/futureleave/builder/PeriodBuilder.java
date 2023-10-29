package com.voltor.futureleave.builder;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.voltor.futureleave.model.Period;
import com.voltor.futureleave.model.User;

@Component
public class PeriodBuilder {

	private Period period;

	public PeriodBuilder() {
		initDefaultData();
	}

	public Period build() {
		Period token = this.period;
		initDefaultData();
		return token;
	}
	
	public Period buildNew() {
		return setUser(null).setId(null).build();
	}

	private void initDefaultData() {
		period = new Period();
		Long randomValue = ThreadLocalRandom.current().nextLong(1, 999999);
		int randomSmallValue = ThreadLocalRandom.current().nextInt(1, 99);
		setId( randomValue );
		setUser( UserBuilder.start().build() );
		setSessionId( randomValue );
		setStartDate(LocalDate.now().plusDays(randomSmallValue));
		setEndDate(period.getStartDate().plusMonths(1));
		setPlannedEventsCount(randomSmallValue);
		setUnplannedEventsCount(randomSmallValue + 10);
	}
	
	public static PeriodBuilder start() {
    	return new PeriodBuilder();
	}
	
	public PeriodBuilder setId( Long id ) {
		period.setId( id );
		return this;
	}
	
	public PeriodBuilder setUser( User user ) {
		period.setUser( user );
		return this;
	}
	 
	public PeriodBuilder setStartDate(LocalDate startDate) {
		period.setStartDate( startDate );
		return this;
	}
 
	public PeriodBuilder setEndDate(LocalDate endDate) {
		period.setEndDate( endDate );
		return this;
	} 

	public PeriodBuilder setPlannedEventsCount(int plannedEventsCount) {
		period.setPlannedEventsCount( plannedEventsCount );
		return this;
	}
 
	public PeriodBuilder setUnplannedEventsCount(int unplannedEventsCount) {
		period.setUnplannedEventsCount( unplannedEventsCount );
		return this;
	}

	public PeriodBuilder setSessionId(Long sessionId) {
		period.setSessionId( sessionId );
		return this;
	}

}
