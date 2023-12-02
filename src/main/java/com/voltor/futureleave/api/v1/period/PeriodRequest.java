package com.voltor.futureleave.api.v1.period;

import java.time.LocalDate;

import com.voltor.futureleave.api.v1.common.AbstractRequest;
import com.voltor.futureleave.model.Period;

public class PeriodRequest extends AbstractRequest<Period> {

	private long sessionId;
	private LocalDate startDate;
	private LocalDate endDate;
	private int plannedEventsCount;
	private int unplannedEventsCount;
	
	@Override
	public Period createEntity() {
		Period period = new Period();
		period.setSessionId(sessionId);
		return updateEntity(period);
	}
	
	@Override
	public Period updateEntity(Period entity) { 
		entity.setStartDate(startDate);
		entity.setEndDate(endDate);
		entity.setPlannedEventsCount(plannedEventsCount);
		entity.setUnplannedEventsCount(unplannedEventsCount);
		return entity;
	}


	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getPlannedEventsCount() {
		return plannedEventsCount;
	}

	public void setPlannedEventsCount(int plannedEventsCount) {
		this.plannedEventsCount = plannedEventsCount;
	}

	public int getUnplannedEventsCount() {
		return unplannedEventsCount;
	}

	public void setUnplannedEventsCount(int unplannedEventsCount) {
		this.unplannedEventsCount = unplannedEventsCount;
	}

}

