package com.voltor.futureleave.api.v1.vehicles;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Sort;

import com.voltor.futureleave.api.v1.ApiConstants;
import com.voltor.futureleave.api.v1.controller.AbstractCRUDControllerTest;
import com.voltor.futureleave.builder.VehicleBuilder;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.VehicleSpecificationBuilder;
import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Vehicle;
import com.voltor.futureleave.service.AbstractService;
import com.voltor.futureleave.service.DateTimeService;
import com.voltor.futureleave.service.VehicleService;

@WebMvcTest( VehiclesController.class )
public class VehiclesControllerTest extends AbstractCRUDControllerTest< Vehicle, VehicleRequest, VehicleResponse > {
	
	@MockBean
	private VehicleService vehicleService;
	
	@SpyBean
	private VehicleSpecificationBuilder specificationBuilder;
	 
	
	@BeforeEach
	public void setup(){
		super.setup();
	}

	@Override
	protected Class<Vehicle> getEntityClass() {
		return Vehicle.class;
	}

	@Override
	protected AbstractService<Vehicle> getService() {
		return this.vehicleService;
	}

	@Override
	protected Vehicle getNewEntity() {
		return VehicleBuilder.start().build();
	}

	@Override
	protected VehicleRequest getNewRequestBean() {
		return convertToRequest( getNewEntity() );
	}

	@Override
	protected Map<List<String>, Sort> getSortingTestParameters() {
		Map< List< String >, Sort > parameters = new HashMap<>();
		parameters.put( List.of( "brandName" ), Sort.by( Sort.Direction.ASC, "brandName") );
		parameters.put( List.of( "brandName,DESC" ), Sort.by( Sort.Direction.DESC, "brandName") );
		
		parameters.put( List.of( "vehicleType" ), Sort.by( Sort.Direction.ASC, "brandType") );
		parameters.put( List.of( "vehicleType,DESC" ), Sort.by( Sort.Direction.DESC, "brandType") );

		parameters.put( List.of( "licensePlate" ), Sort.by( Sort.Direction.ASC, "licensePlate") );
		parameters.put( List.of( "licensePlate,DESC" ), Sort.by( Sort.Direction.DESC, "licensePlate") );
		
		parameters.put( List.of( "startDate" ), Sort.by( Sort.Direction.ASC, "startDate") );
		parameters.put( List.of( "startDate,DESC" ), Sort.by( Sort.Direction.DESC, "startDate") );
		
		parameters.put( List.of( "endDate" ), Sort.by( Sort.Direction.ASC, "endDate") );
		parameters.put( List.of( "endDate,DESC" ), Sort.by( Sort.Direction.DESC, "endDate") );
		return parameters;
	}

	@Override
	protected List<Function< Vehicle, Object >> getValueToBeUpdated( VehicleRequest request ) {
		List< Function< Vehicle, Object >  > parameters = new LinkedList<>();
		parameters.add( Vehicle::getBrandName );
		parameters.add( Vehicle::getBrandType );
		parameters.add( Vehicle::getLicensePlate );
		parameters.add( Vehicle::getStartDate );
		parameters.add( Vehicle::getEndDate );
		return parameters;
	}

	@Override
	protected Map< List< String >, List< SearchCriteria > > getFilteringTestParameters() {
		Map<List<String>, List<SearchCriteria>>  filteringTestParameters = new HashMap<>();
		filteringTestParameters.put(
				List.of( "brandName=9556","brandName!=65885" ),
				List.of( new SearchCriteria("brandName", FilteringOperation.EQUAL, "9556"), 
						new SearchCriteria("brandName", FilteringOperation.NOT_EQUAL, "65885") ) );

		filteringTestParameters.put(
				List.of( "vehicleType=9556","vehicleType!=65885" ),
				List.of( new SearchCriteria("brandType", FilteringOperation.EQUAL, "9556"), 
						new SearchCriteria("brandType", FilteringOperation.NOT_EQUAL, "65885") ) );

		filteringTestParameters.put(
				List.of( "licensePlate=9556","licensePlate!=65885" ),
				List.of( new SearchCriteria("licensePlate", FilteringOperation.EQUAL, "9556"), 
						new SearchCriteria("licensePlate", FilteringOperation.NOT_EQUAL, "65885") ) );
		
		ZoneId orgTimeZoneId = null;
		
		filteringTestParameters.put(
				List.of( "startDate>=2006-01-31","startDate>2006-03-15","startDate=2008-01-31",
						"startDate!=2019-01-31","startDate<2020-01-31","startDate<=2022-01-31" ),
				List.of( 
						new SearchCriteria("startDate", FilteringOperation.GREATER_OR_EQUAL, DateTimeService.getFirstMomentOfLocalDate( "2006-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("startDate", FilteringOperation.GREATER_THEN, DateTimeService.getFirstMomentOfLocalDate( "2006-03-15", orgTimeZoneId ) ), 
						new SearchCriteria("startDate", FilteringOperation.EQUAL, DateTimeService.getFirstMomentOfLocalDate( "2008-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("startDate", FilteringOperation.NOT_EQUAL, DateTimeService.getFirstMomentOfLocalDate( "2019-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("startDate", FilteringOperation.LESS_THEN, DateTimeService.getFirstMomentOfLocalDate( "2020-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("startDate", FilteringOperation.LESS_OR_EQUAL, DateTimeService.getFirstMomentOfLocalDate( "2022-01-31", orgTimeZoneId ) ) ) );
		
		filteringTestParameters.put(
				List.of( "endDate>=2006-01-31","endDate>2006-03-15","endDate=2008-01-31",
						"endDate!=2019-01-31","endDate<2020-01-31","endDate<=2022-01-31" ),
				List.of( 
						new SearchCriteria("endDate", FilteringOperation.GREATER_OR_EQUAL, DateTimeService.getLastMomentOfLocalDate( "2006-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("endDate", FilteringOperation.GREATER_THEN, DateTimeService.getLastMomentOfLocalDate( "2006-03-15", orgTimeZoneId ) ), 
						new SearchCriteria("endDate", FilteringOperation.EQUAL, DateTimeService.getLastMomentOfLocalDate( "2008-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("endDate", FilteringOperation.NOT_EQUAL, DateTimeService.getLastMomentOfLocalDate( "2019-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("endDate", FilteringOperation.LESS_THEN, DateTimeService.getLastMomentOfLocalDate( "2020-01-31", orgTimeZoneId ) ), 
						new SearchCriteria("endDate", FilteringOperation.LESS_OR_EQUAL, DateTimeService.getLastMomentOfLocalDate( "2022-01-31", orgTimeZoneId ) ) ) );
		return filteringTestParameters;
	}

	@Override
	protected EntityFilterSpecificationsBuilder<Vehicle> getSpecificationsBuilder() {
		return specificationBuilder;
	}

	@Override
	protected HashMap<Consumer<VehicleRequest>, Pair<Function<Vehicle, Object>, Function<Vehicle, Object>>> getCreateOptionalFieldsTestParameters() {
		return new HashMap<>();
	}

	@Override
	protected Map<Map<String, Supplier<Object>>, String> getPatchExceptionsValuesTestParameters() {
		Map< Map< String, Supplier< Object > >, String > parameters = new HashMap<>(); 

		parameters.put( Map.of( "licensePlate", () -> null ), "Error occurred. licensePlate: must not be blank" );
		parameters.put( Map.of( "licensePlate", () -> "" ), "Error occurred. licensePlate: must not be blank" );
		parameters.put( Map.of( "licensePlate", () -> "  " ), "Error occurred. licensePlate: must not be blank" );
		
		parameters.put(  Map.of( "startDate", () -> null ), "Error occurred. startDate: must not be null" );
		parameters.put(  Map.of( "endDate", () -> null ), "Error occurred. endDate: must not be null" );
		
		
		parameters.put(  Map.of( "startDate", () -> "2025-03-15", "endDate", () -> "2020-03-15" ), 
				"Error occurred. vehicleRequest: startDate must not be after endDate" );
		
		return parameters;
	}

	@Override
	protected Map<Map<String, Object>, Pair<Function<Vehicle, Object>, Object>> getPatchValuesTestParameters() {
		ZoneId orgTimeZoneId = null;
		Map< Map< String, Object >, Pair< Function< Vehicle, Object >, Object > > parameters = new HashMap<>();
		parameters.put(
				Map.of( "brandName", "test2mers" ),
				Pair.of( Vehicle :: getBrandName, "test2mers" ) );
		parameters.put(
				Map.of( "vehicleType", "minicar" ),
				Pair.of( Vehicle :: getBrandType, "minicar" ) );
		parameters.put(
				Map.of( "licensePlate", "d9s33s" ),
				Pair.of( Vehicle :: getLicensePlate, "d9s33s" ) );
		parameters.put(
				Map.of( "startDate", "2020-10-25" ),
				Pair.of( Vehicle :: getStartDate, DateTimeService.getFirstMomentOfDate( LocalDate.of( 2020, 10, 25 ), orgTimeZoneId ) ) );
		parameters.put(
				Map.of( "endDate", "2025-11-24" ),
				Pair.of( Vehicle :: getEndDate, DateTimeService.getLastMomentOfDate( LocalDate.of( 2025, 11, 24 ), orgTimeZoneId ) ) );
		parameters.put(
				Map.of( "endDate", "2025-11-24", "startDate", "2025-11-24"   ),
				Pair.of( Vehicle :: getEndDate, DateTimeService.getLastMomentOfDate( LocalDate.of( 2025, 11, 24 ), orgTimeZoneId ) ) );
		return parameters;
	}

	@Override
	protected VehicleRequest convertToRequest( Vehicle vehicle ) {
		VehicleRequest vehicleRequest = new VehicleRequest();
		vehicleRequest.setBrandName( vehicle.getBrandName() );
		vehicleRequest.setVehicleType( vehicle.getBrandType() );
		vehicleRequest.setLicensePlate( vehicle.getLicensePlate() );
		ZoneId orgTimeZoneId = null;
		vehicleRequest.setStartDate( LocalDate.ofInstant( vehicle.getStartDate().toInstant(), orgTimeZoneId ));
		vehicleRequest.setEndDate( LocalDate.ofInstant( vehicle.getEndDate().toInstant(), orgTimeZoneId ));
		return vehicleRequest;
	}

	@Override
	protected VehicleResponse convertToResponse( Vehicle vehicle ) {
		VehicleResponse vehicleResponse = new VehicleResponse( vehicle );
		vehicleResponse.setBrandName( vehicle.getBrandName() );
		vehicleResponse.setVehicleType( vehicle.getBrandType() );
		vehicleResponse.setLicensePlate( vehicle.getLicensePlate() );
		ZoneId orgTimeZoneId = null;
		vehicleResponse.setStartDate( LocalDate.ofInstant( vehicle.getStartDate().toInstant(), orgTimeZoneId ));
		vehicleResponse.setEndDate( LocalDate.ofInstant( vehicle.getEndDate().toInstant(), orgTimeZoneId ));
		return vehicleResponse;
	}

	@Override
	protected Map<Consumer<VehicleRequest>, String> getCreateWithWrongValuesTestParameters() {
		Map< Consumer< VehicleRequest >, String > parameters = getGeneralWrongValuesTestParameters();
		parameters.put( request -> request.setBcId( "" ), "Error occurred. bcId: must not be blank" );
		parameters.put( request -> request.setBcId( "   " ), "Error occurred. bcId: must not be blank" );
		parameters.put( request -> request.setBcId( null ), "Error occurred. bcId: must not be blank" );
		return parameters;
	}
	
	protected Map< Consumer< VehicleRequest >, String > getUpdateWithWrongValuesTestParameters(){
		Map< Consumer< VehicleRequest >, String > parameters = getGeneralWrongValuesTestParameters();
		parameters.put( request -> request.setStartDate( null ), "Error occurred. startDate: must not be null" );
		parameters.put( request -> request.setEndDate( null ), "Error occurred. endDate: must not be null" );
		return parameters;
	}
	
	protected Map< Consumer< VehicleRequest >, String > getGeneralWrongValuesTestParameters() {
		Map< Consumer< VehicleRequest >, String > parameters = new HashMap<>();

		parameters.put( request -> {
    			request.setStartDate( LocalDate.now() );
    			request.setEndDate( LocalDate.now().minusDays( 2 ) );
    		}, "Error occurred. vehicleRequest: startDate must not be after endDate" );
		parameters.put( request -> request.setLicensePlate("" ), "Error occurred. licensePlate: must not be blank" );
		parameters.put( request -> request.setLicensePlate( "   " ), "Error occurred. licensePlate: must not be blank" );
		parameters.put( request -> request.setLicensePlate( null ), "Error occurred. licensePlate: must not be blank" );		
		return parameters;
	} 

	@Override
	protected String getControllerPath() {
		return ApiConstants.V1_VEHICLES_ENDPOINT;
	}

}
