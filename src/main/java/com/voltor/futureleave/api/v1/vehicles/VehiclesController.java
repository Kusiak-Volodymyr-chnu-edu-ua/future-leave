package com.voltor.futureleave.api.v1.vehicles;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voltor.futureleave.api.v1.ApiConstants;
import com.voltor.futureleave.api.v1.common.AbstractController;
import com.voltor.futureleave.api.v1.common.UpdateValidationGroup;
import com.voltor.futureleave.api.v1.exception.PatchFieldConstraintViolationException;
import com.voltor.futureleave.filtering.FilterableProperty;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.VehicleSpecificationBuilder;
import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.model.Vehicle;
import com.voltor.futureleave.service.AbstractService;
import com.voltor.futureleave.service.DateTimeService;
import com.voltor.futureleave.service.VehicleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

@Tag( name = "Vehicle" )
@RestController
@RequestMapping( ApiConstants.V1_VEHICLES_ENDPOINT )
public class VehiclesController extends AbstractController< Vehicle, VehicleRequest, VehicleResponse> {
	
	public static final LocalDate DEFAULT_START_DATE = LocalDate.of( 2000, 1, 1 );
	public static final LocalDate DEFAULT_END_DATE = LocalDate.of( 2099, 12, 31 );
	
	
	@Autowired
	private VehicleSpecificationBuilder specificationBuilder;
	
	@Autowired
	private VehicleService service;
	
	private Validator validator;

	public VehiclesController( ValidatorFactory validatorFactory ) {
		validator = validatorFactory.getValidator();
	} 
	
	@Override
	protected Vehicle executeEntityCreate( Vehicle vehicle, VehicleRequest request ) {
		ZoneId orgTimeZoneId = null;
		vehicle.setStartDate( DateTimeService.getFirstMomentOfDate( request.getStartDate(), orgTimeZoneId ));
		vehicle.setEndDate( DateTimeService.getLastMomentOfDate( request.getEndDate(), orgTimeZoneId ));
		return super.executeEntityCreate( vehicle, request );
	}
	
	@Override
	protected Vehicle executeEntityUpdate( Vehicle entity, VehicleRequest request) {
		ZoneId orgTimeZoneId = getOrgTimeZone();
		entity.setStartDate( DateTimeService.getFirstMomentOfDate( request.getStartDate(), orgTimeZoneId ) );
		entity.setEndDate( DateTimeService.getLastMomentOfDate( request.getEndDate(), orgTimeZoneId ) );
		return super.executeEntityUpdate( entity, request );
	}

	@Override
	public AbstractService<Vehicle> getService() {
		return this.service;
	}

	@Override
	public VehicleResponse convertEntityToResponse( Vehicle vehicle, List<String> entitiesToExpand ) {
		VehicleResponse vehicleResponse = new VehicleResponse( vehicle );
		vehicleResponse.setBrandName( vehicle.getBrandName() );
		vehicleResponse.setLicensePlate( vehicle.getLicensePlate() );
		vehicleResponse.setVehicleType( vehicle.getBrandType() );
		ZoneId orgTimeZoneId = getOrgTimeZone();
		vehicleResponse.setStartDate( LocalDate.ofInstant( vehicle.getStartDate().toInstant(), orgTimeZoneId ));
		vehicleResponse.setEndDate( LocalDate.ofInstant( vehicle.getEndDate().toInstant(), orgTimeZoneId ));
		return vehicleResponse;
	}

	@Override
	public Class<Vehicle> getEntityClass() {
		return Vehicle.class;
	}
	
	@Override
	public EntityFilterSpecificationsBuilder<Vehicle> getFilterSpecificationsBuilder() {
		return specificationBuilder;
	} 
	
	@Override
	protected Object convertValueForCriteria( String key, FilteringOperation operation, String value, FilterableProperty< Vehicle > filterableProperty ) {
		switch ( key ) {
    		case "startDate":
    			return DateTimeService.getFirstMomentOfLocalDate( value, getOrgTimeZone() );
    		case "endDate":
    			return DateTimeService.getLastMomentOfLocalDate( value, getOrgTimeZone() );
    		default:
    			return super.convertValueForCriteria( key, operation, value, filterableProperty );
		}
	}
	
	private ZoneId getOrgTimeZone() {
		return null;
	}
	
	@Override
	protected String mapDBProperty( String property) {
		switch ( property ) {
    		case "vehicleType":
    			return "brandType";
    		default:
    			return super.mapDBProperty( property );
		}
		
	}
	
	@Override
	protected void patchFields( Vehicle entity, Map< String, Object > request ) {
		request.entrySet().forEach( requestedField -> patchField( entity, requestedField ) );
		if( entity.getStartDate().isAfter( entity.getEndDate() ) ) {
			throw new PatchFieldConstraintViolationException( "vehicleRequest", "startDate must not be after endDate" );
		}
	}
	
	protected void patchField( Vehicle entity, Entry< String, Object > requestedField ) {
		switch( requestedField.getKey() ) {
			case "brandName":
				String brandName = conversionService.convert( requestedField.getValue(), String.class );
				validatePatchField( "brandName", brandName );
				entity.setBrandName( brandName );
				return;
			case "vehicleType":
				String brandType = conversionService.convert( requestedField.getValue(), String.class );
				validatePatchField( "vehicleType", brandType );
				entity.setBrandType( brandType );
				return;
			case "licensePlate":
				String licensePlate = conversionService.convert( requestedField.getValue(), String.class );
				validatePatchField( "licensePlate", licensePlate );
				entity.setLicensePlate( licensePlate );
				return;
			case "startDate":
				LocalDate startDate = conversionService.convert( requestedField.getValue(), LocalDate.class );
				validatePatchField( "startDate", startDate );
				entity.setStartDate( DateTimeService.getFirstMomentOfDate( startDate, getOrgTimeZone() ) );
				return;
			case "endDate":
				LocalDate endDate = conversionService.convert( requestedField.getValue(), LocalDate.class );
				validatePatchField( "endDate", endDate );
				entity.setEndDate( DateTimeService.getLastMomentOfDate( endDate, getOrgTimeZone() ) );
				return;
    		default:
    			return;
    	}
	}
	
	private void validatePatchField( String propertyName, Object value ) {
		Set< ConstraintViolation< VehicleRequest > > validationResult = validator
				.validateValue( VehicleRequest.class, propertyName, value, UpdateValidationGroup.class, Default.class );
		if(validationResult.isEmpty()) {
			return;
		}
		throw new PatchFieldConstraintViolationException( validationResult );
	}

}