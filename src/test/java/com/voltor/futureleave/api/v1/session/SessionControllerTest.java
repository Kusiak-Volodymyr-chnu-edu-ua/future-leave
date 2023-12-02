package com.voltor.futureleave.api.v1.session;

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
import com.voltor.futureleave.builder.SessionBuilder;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.filtering.predicate.SessionSpecificationBuilder;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.service.AbstractService;
import com.voltor.futureleave.service.SessionService;

@WebMvcTest( SessionController.class )
public class SessionControllerTest extends AbstractCRUDControllerTest<Session, SessionRequest, SessionResponse>{
	
	@MockBean
	private SessionService sessionService;
	
	@SpyBean
	private SessionSpecificationBuilder specificationBuilder;
	  

	@Override
	protected Class<Session> getEntityClass() {
		return Session.class;
	}

	@Override
	protected AbstractService<Session> getService() {
		return this.sessionService;
	}

	@Override
	protected Session getNewEntity() {
		return SessionBuilder.start().build();
	}

	@Override
	protected SessionRequest getNewRequestBean() {
		return convertToRequest( getNewEntity() );
	}

	@Override
	protected Map<List<String>, Sort> getSortingTestParameters() {
		Map< List< String >, Sort > parameters = new HashMap<>();
		parameters.put( List.of( "name" ), Sort.by( Sort.Direction.ASC, "name") );
		parameters.put( List.of( "name,DESC" ), Sort.by( Sort.Direction.DESC, "name") );
		return parameters;
	}

	@Override
	protected List<Function< Session, Object >> getValueToBeUpdated( SessionRequest request ) {
		List< Function< Session, Object >  > parameters = new LinkedList<>();
		parameters.add( Session::getName );
		return parameters;
	}

	@Override
	protected Map< List< String >, List< SearchCriteria > > getFilteringTestParameters() {
		Map<List<String>, List<SearchCriteria>>  filteringTestParameters = new HashMap<>();
		filteringTestParameters.put(
				List.of( "name=9556","name!=65885" ),
				List.of( new SearchCriteria("name", FilteringOperation.EQUAL, "9556"), 
						new SearchCriteria("name", FilteringOperation.NOT_EQUAL, "65885") ) );
		return filteringTestParameters;
	}

	@Override
	protected EntityFilterSpecificationsBuilder<Session> getSpecificationsBuilder() {
		return specificationBuilder;
	}

	@Override
	protected HashMap<Consumer<SessionRequest>, Pair<Function<Session, Object>, Function<Session, Object>>> getCreateOptionalFieldsTestParameters() {
		return new HashMap<>();
	}

	@Override
	protected Map<Map<String, Supplier<Object>>, String> getPatchExceptionsValuesTestParameters() {
		Map< Map< String, Supplier< Object > >, String > parameters = new HashMap<>();
		return parameters;
	}

	@Override
	protected Map<Map<String, Object>, Pair<Function<Session, Object>, Object>> getPatchValuesTestParameters() {
		Map< Map< String, Object >, Pair< Function< Session, Object >, Object > > parameters = new HashMap<>();
		parameters.put(
				Map.of( "name", "test2mers" ),
				Pair.of( Session :: getName, "test2mers" ) );
		return parameters;
	}

	@Override
	protected SessionRequest convertToRequest( Session session ) {
		SessionRequest sessionRequest = new SessionRequest();
		sessionRequest.setName( session.getName() );
		return sessionRequest;
	}

	@Override
	protected SessionResponse convertToResponse( Session session ) {
		SessionResponse sessionResponse = new SessionResponse( session );
		sessionResponse.setName( session.getName() );
		return sessionResponse;
	}

	@Override
	protected Map<Consumer<SessionRequest>, String> getCreateWithWrongValuesTestParameters() {
		return  getGeneralWrongValuesTestParameters();
	} 
	
	protected Map< Consumer< SessionRequest >, String > getGeneralWrongValuesTestParameters() {		
		return new HashMap<>();
	} 

	@Override
	protected String getControllerPath() {
		return ApiConstants.V1_SESSION_ENDPOINT;
	}

}
