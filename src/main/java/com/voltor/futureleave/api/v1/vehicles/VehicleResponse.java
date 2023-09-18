package com.voltor.futureleave.api.v1.vehicles;

import java.time.LocalDate;

import com.voltor.futureleave.api.v1.common.AbstractResponse;
import com.voltor.futureleave.model.Vehicle;

import io.swagger.v3.oas.annotations.media.Schema;

public class VehicleResponse extends AbstractResponse {
	
	@Schema( description = "The brand name of the vehicle<br/>"
        		+ "Sortable<br/>"
        		+ "Filterable: EQUAL(=), NOT_EQUAL(!=)",
			example = "Daf V-100" )
	private String brandName;
	
	@Schema( description = "The type of the vehicle<br/>"
        		+ "Sortable<br/>"
        		+ "Filterable: EQUAL(=), NOT_EQUAL(!=)",
			example = "FT XF 105 4x2" )
	private String vehicleType;
	
	@Schema( description = "The bcId of the vehicle<br/>"
        		+ "Sortable<br/>"
        		+ "Filterable: EQUAL(=), NOT_EQUAL(!=)",
    		example = "228322" )
	private String licensePlate;
	
	@Schema( description  = "The startDate of the vehicle<br/>"
        			+ "Expected format is yyyy-MM-dd<br/>"
            		+ "Sortable<br/>"
            		+ "Filterable: EQUAL(=), NOT_EQUAL(!=), GREATER_THEN(>), GREATER_OR_EQUAL(>=), LESS_THEN(<), LESS_OR_EQUAL(<=)",
            example = "2020-01-31"  )
	private LocalDate startDate;
	
	@Schema( description  = "The endDate of the vehicle<br/>"
        			+ "Expected format is yyyy-MM-dd<br/>"
            		+ "Sortable<br/>"
            		+ "Filterable: EQUAL(=), NOT_EQUAL(!=), GREATER_THEN(>), GREATER_OR_EQUAL(>=), LESS_THEN(<), LESS_OR_EQUAL(<=)",
            example = "2099-01-31"  )
	private LocalDate endDate;
	
	public VehicleResponse(Vehicle entity) {
		super( entity.getId() );
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

