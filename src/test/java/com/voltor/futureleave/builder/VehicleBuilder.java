package com.voltor.futureleave.builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.voltor.futureleave.model.SupportedTimeZone;
import com.voltor.futureleave.model.Vehicle;

@Component
public class VehicleBuilder {

	private Vehicle vehicle;
	private ZoneId zoneId;
	
	public static VehicleBuilder start() {
    	return new VehicleBuilder();
	}
	
	public VehicleBuilder() {
		initDefaultData();
	}

	public Vehicle build() {
		Vehicle vehicle = this.vehicle;
		initDefaultData();
		return vehicle;
	}
	
	private void initDefaultData() {
		this.vehicle = new Vehicle();
		zoneId = ZoneId.of( SupportedTimeZone.getDefaultName() );
		Long randomValue = ThreadLocalRandom.current().nextLong( 1, 999999 );
		setId( randomValue );
		setNumber( "number_" + randomValue );  
		setBrandName( "brandname_" + randomValue );
		setBrandType( "brandtype_" + randomValue );
		setLicensePlate( "licensePlate_" + randomValue );
		setExportId( "exportId_" + randomValue );
		int randDays = ThreadLocalRandom.current().nextInt( 1, 100 );
		setStartDate( LocalDate.now().minusDays( randDays ));
		setEndDate( LocalDate.now().plusDays( randDays));
	}

	
	public VehicleBuilder setId( Long id ) {
		this.vehicle.setId( id );
		return this;
	}
	
	public VehicleBuilder setBrandName( String brandName ) {
		this.vehicle.setBrandName( brandName );
		return this;
	}
 
	public VehicleBuilder setBrandType( String brandType ) {
		this.vehicle.setBrandType( brandType );
		return this;
	}
	
	public VehicleBuilder setLicensePlate( String licensePlate ) {
		this.vehicle.setLicensePlate( licensePlate );
		return this;
	}
	
	public VehicleBuilder setNumber( String number ) {
		this.vehicle.setNumber( number );
		
		return this;
	}
	public VehicleBuilder setStartDate( LocalDate startDate ) {
		this.vehicle.setStartDate( ZonedDateTime.of( startDate, LocalTime.MIN, zoneId ) );
		return this;
	}
	
	public VehicleBuilder setEndDate( LocalDate endDate ) {
		this.vehicle.setEndDate( ZonedDateTime.of( endDate, LocalTime.MAX, zoneId ) );
		return this;
	}
	
	public VehicleBuilder zoneId( ZoneId zoneId ) {
		this.zoneId = zoneId;
		setStartDate( vehicle.getStartDate().toLocalDate() );
		setEndDate( vehicle.getEndDate().toLocalDate() );
		return this;
	}
	
	public VehicleBuilder setExportId( String exportId ) {
		this.vehicle.setExportId( exportId );
		return this;
	}

}
