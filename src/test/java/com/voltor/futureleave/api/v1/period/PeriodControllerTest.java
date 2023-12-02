package com.voltor.futureleave.api.v1.period;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Sort;

import com.voltor.futureleave.api.v1.ApiConstants;
import com.voltor.futureleave.api.v1.controller.AbstractCRUDControllerTest;
import com.voltor.futureleave.builder.PeriodBuilder;
import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.filtering.predicate.PeriodSpecificationBuilder;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Period;
import com.voltor.futureleave.service.AbstractService;
import com.voltor.futureleave.service.PeriodService;

@WebMvcTest( PeriodController.class )
public class PeriodControllerTest extends AbstractCRUDControllerTest<Period, PeriodRequest, PeriodResponse>{
	
	@MockBean
	private PeriodService periodService;
	
	@SpyBean
	private PeriodSpecificationBuilder specificationBuilder;
	  

	@Override
	protected Class<Period> getEntityClass() {
		return Period.class;
	}

	@Override
	protected AbstractService<Period> getService() {
		return this.periodService;
	}

	@Override
	protected Period getNewEntity() {
		return PeriodBuilder.start().build();
	}

	@Override
	protected PeriodRequest getNewRequestBean() {
		return convertToRequest( getNewEntity() );
	}

	@Override
	protected Map<List<String>, Sort> getSortingTestParameters() {
		Map< List< String >, Sort > parameters = new HashMap<>();
		parameters.put( List.of( "startDate" ), Sort.by( Sort.Direction.ASC, "startDate") );
		parameters.put( List.of( "startDate,DESC" ), Sort.by( Sort.Direction.DESC, "startDate") );

		parameters.put( List.of( "endDate" ), Sort.by( Sort.Direction.ASC, "endDate") );
		parameters.put( List.of( "endDate,DESC" ), Sort.by( Sort.Direction.DESC, "endDate") );
		

		parameters.put( List.of( "plannedEventsCount" ), Sort.by( Sort.Direction.ASC, "plannedEventsCount") );
		parameters.put( List.of( "plannedEventsCount,DESC" ), Sort.by( Sort.Direction.DESC, "plannedEventsCount") );
		

		parameters.put( List.of( "unplannedEventsCount" ), Sort.by( Sort.Direction.ASC, "unplannedEventsCount") );
		parameters.put( List.of( "unplannedEventsCount,DESC" ), Sort.by( Sort.Direction.DESC, "unplannedEventsCount") );
		return parameters;
	}

	@Override
	protected List<Function< Period, Object >> getValueToBeUpdated( PeriodRequest request ) {
		List< Function< Period, Object >  > parameters = new LinkedList<>();
		parameters.add( Period::getStartDate );
		parameters.add( Period::getEndDate );
		parameters.add( Period::getPlannedEventsCount );
		parameters.add( Period::getUnplannedEventsCount );
		return parameters;
	}

	@Override
	protected Map< List< String >, List< SearchCriteria > > getFilteringTestParameters() {
		Map<List<String>, List<SearchCriteria>>  filteringTestParameters = new HashMap<>();
//		filteringTestParameters.put(
//				List.of( "sessionId=9556","sessionId!=65885" ),
//				List.of( new SearchCriteria("sessionId", FilteringOperation.EQUAL, "9556"), 
//						new SearchCriteria("sessionId", FilteringOperation.NOT_EQUAL, "65885") ) );
		return filteringTestParameters;
	}

	@Override
	protected EntityFilterSpecificationsBuilder<Period> getSpecificationsBuilder() {
		return specificationBuilder;
	}

	@Override
	protected HashMap<Consumer<PeriodRequest>, Pair<Function<Period, Object>, Function<Period, Object>>> getCreateOptionalFieldsTestParameters() {
		return new HashMap<>();
	}

	@Override
	protected Map<Map<String, Supplier<Object>>, String> getPatchExceptionsValuesTestParameters() {
		Map< Map< String, Supplier< Object > >, String > parameters = new HashMap<>();
		return parameters;
	}

	@Override
	protected Map<Map<String, Object>, Pair<Function<Period, Object>, Object>> getPatchValuesTestParameters() {
		Map< Map< String, Object >, Pair< Function< Period, Object >, Object > > parameters = new HashMap<>();
		parameters.put(
				Map.of( "startDate", "2022-01-01" ),
				Pair.of( Period :: getStartDate, LocalDate.of(2022, 1, 1) ) );
		
		parameters.put(
				Map.of( "endDate", "2022-01-01" ),
				Pair.of( Period :: getEndDate, LocalDate.of(2022, 1, 1) ) );
		
		parameters.put(
				Map.of( "plannedEventsCount", "21" ),
				Pair.of( Period :: getPlannedEventsCount, 21 ) );
		
		parameters.put(
				Map.of( "unplannedEventsCount", "23" ),
				Pair.of( Period :: getUnplannedEventsCount, 23 ) );

		return parameters;
	}

	@Override
	protected PeriodRequest convertToRequest( Period period ) {
		PeriodRequest periodRequest = new PeriodRequest();
		periodRequest.setSessionId( period.getSessionId() );
		periodRequest.setStartDate( period.getStartDate() );
		periodRequest.setEndDate( period.getEndDate() );
		periodRequest.setPlannedEventsCount( period.getPlannedEventsCount() );
		periodRequest.setUnplannedEventsCount( period.getUnplannedEventsCount() );
		return periodRequest;
	}

	@Override
	protected PeriodResponse convertToResponse( Period period ) {
		PeriodResponse periodResponse = new PeriodResponse( period );
		periodResponse.setSessionId( period.getSessionId() );
		periodResponse.setStartDate( period.getStartDate() );
		periodResponse.setEndDate( period.getEndDate() );
		periodResponse.setPlannedEventsCount( period.getPlannedEventsCount() );
		periodResponse.setUnplannedEventsCount( period.getUnplannedEventsCount() );
		return periodResponse;
	}

	@Override
	protected Map<Consumer<PeriodRequest>, String> getCreateWithWrongValuesTestParameters() {
		return  getGeneralWrongValuesTestParameters();
	} 
	
	protected Map< Consumer< PeriodRequest >, String > getGeneralWrongValuesTestParameters() {		
		return new HashMap<>();
	} 

	@Override
	protected String getControllerPath() {
		return ApiConstants.V1_PERIOD_ENDPOINT;
	}

}
