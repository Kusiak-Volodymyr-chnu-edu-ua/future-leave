package com.voltor.futureleave.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fl_period")
public class Period extends SessionTenencyEntity implements Identifiable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	 
	@Column
	private LocalDate startDate;
 
	@Column
	private LocalDate endDate;

	@Column
	private int plannedEventsCount;
	
	@Column
	private int unplannedEventsCount;
	  

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		Period other = (Period) o;
		return Objects.equals( id, other.id );
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}
}
