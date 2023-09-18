package com.voltor.futureleave.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle" )
public class Vehicle extends SessionTenencyEntity implements Identifiable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(name = "brand_name", nullable = true)
	private String brandName;

	@Column(name = "brand_type", nullable = true)
	private String brandType;

	@Column(name = "license_plate", nullable = false)
	private String licensePlate;

	@Column(nullable = false)
	private String number;

	@Column(name = "start_date", nullable = false)
	private ZonedDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private ZonedDateTime endDate;

	@Column(name = "external_id", nullable = false)
	private String externalId;

	/* Used by export system [Activities] */
	@Column(name = "export_id", nullable = true)
	private String exportId;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId( Long id ) {
		this.id = id;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName( String brandName ) {
		this.brandName = brandName;
	}

	public String getBrandType() {
		return brandType;
	}

	public void setBrandType( String brandType ) {
		this.brandType = brandType;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate( String licensePlate ) {
		this.licensePlate = licensePlate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber( String number ) {
		this.number = number;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate( ZonedDateTime startDate ) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate( ZonedDateTime endDate ) {
		this.endDate = endDate;
	}

	public String getExportId() {
		return exportId;
	}

	public void setExportId( String exportId ) {
		this.exportId = exportId;
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof Vehicle ) )
			return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals( id, other.id );
	}
    
}
