package com.voltor.futureleave.api.v1.vehicles;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.voltor.futureleave.api.v1.common.AbstractRequest;
import com.voltor.futureleave.api.v1.common.CreateValidationGroup;
import com.voltor.futureleave.api.v1.common.UpdateValidationGroup;
import com.voltor.futureleave.model.Vehicle;
import com.voltor.futureleave.validation.interval.IntervalFrom;
import com.voltor.futureleave.validation.interval.IntervalTill;
import com.voltor.futureleave.validation.interval.ValidInterval;

import io.swagger.v3.oas.annotations.media.Schema;

@ValidInterval
public class VehicleRequest extends AbstractRequest<Vehicle> {

	@Schema( description = "The bcId of the vehicle<br/>"
    			+ "Used only for create, required, unique",
    		example = "228322" )
	@NotBlank( groups = CreateValidationGroup.class )
	private String bcId;
	
	@Schema( description = "The brand name of the vehicle",
			example = "Daf V-100" )
	private String brandName;
	
	@Schema( description = "The type of the vehicle",
			example = "FT XF 105 4x2" )
	private String vehicleType;
	
	@Schema( description = "The license plate of the vehicle<br/>"
				+ "Required",
			example = "228322" )
	@NotBlank
	private String licensePlate;
	
	@Schema( description  = "The startDate of the vehicle<br/>"
    			+ "Expected format is yyyy-MM-dd<br/>"
    			+ "Optional for create, required for update",
			example = "2020-01-31"  )
	@IntervalFrom
	@NotNull( groups = UpdateValidationGroup.class )
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  
	private LocalDate startDate;
	
	@Schema( description  = "The endDateof the vehicle<br/>"
    			+ "Expected format is yyyy-MM-dd<br/>"
    			+ "Optional for create, required for update",
			example = "2099-01-31"  )
	@IntervalTill
	@NotNull( groups = UpdateValidationGroup.class )
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  
	private LocalDate endDate;

	@Override
	public Vehicle createEntity() {
		Vehicle vehicle = new Vehicle();
		if ( startDate == null ) { startDate = VehiclesController.DEFAULT_START_DATE; }
    	if ( endDate == null ) { endDate = VehiclesController.DEFAULT_END_DATE; }
		return updateEntity( vehicle );
	}

	@Override
	public Vehicle updateEntity( Vehicle vehicle ) {
		vehicle.setBrandName( this.brandName );
		vehicle.setBrandType( this.vehicleType );
		vehicle.setLicensePlate( this.licensePlate );
		return vehicle;
	}
	
	public String getBcId() {
		return bcId;
	}

	public void setBcId( String bcId ) {
		this.bcId = bcId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
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

}
