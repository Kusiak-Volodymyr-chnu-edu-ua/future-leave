package com.voltor.futureleave.api.v1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.voltor.futureleave.api.v1.common.AbstractRequest;
import com.voltor.futureleave.api.v1.common.AbstractResponse;
import com.voltor.futureleave.api.v1.common.PageResponse;
import com.voltor.futureleave.api.v1.exception.LocalizedExceptionUtil;
import com.voltor.futureleave.api.v1.exception.ObjectNotFoundException;
import com.voltor.futureleave.dao.exception.RemovalNotAllowedException;
import com.voltor.futureleave.dao.exception.UpdateNotAllowedException;
import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.service.AbstractService;
 
public abstract class AbstractCRUDControllerTest<
		Entity extends Identifiable,
		Request extends AbstractRequest< Entity >,
		Response extends AbstractResponse>
		extends SpringBasedControllerTest {

	protected static final Logger LOGGER = LoggerFactory.getLogger( AbstractCRUDControllerTest.class );

	protected ResultCaptor< Entity > resultCaptor;
	
	@BeforeEach
	public void setup(){
		resultCaptor = new ResultCaptor<>();
	}
	
	@Test
	public void testItemCreation() throws Exception {
		
		mockServiceCreateOrUpdateMethod( resultCaptor, whenCreateInService( any( getEntityClass() ) ) );
		
		ResultActions resultAction = mvc.perform( post( getControllerPath() )
				.content( getJson( getNewRequestBean() ) )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isCreated() );
				
		assertNotNull( resultCaptor.getResult() );
		
		Response expectedResponse = convertToResponse( resultCaptor.getResult() );
		resultAction.andExpect( content().json( getJson( expectedResponse ) ) );
	}
	
	protected <T> void mockServiceCreateOrUpdateMethod( ResultCaptor< Entity > resultCaptor, OngoingStubbing<T> ongoingStubbing){
		ongoingStubbing.thenAnswer( invocation -> {
			Entity entity = invocation.getArgument( 0, getEntityClass() );
			entity.setId( getRandomId() );
			resultCaptor.setResult(entity);
			return entity;});
	}
	
	@Test
	public void testItemCreationOptionalFields() throws Exception {
		String path = getControllerPath();
		mockServiceCreateOrUpdateMethod( resultCaptor, whenCreateInService( any( getEntityClass() ) ) );
		getCreateOptionalFieldsTestParameters().forEach( ( requestedMap, valueProvider ) -> doOptionalFieldsCreationTest( path, requestedMap, valueProvider ) );
	}
	
	private void doOptionalFieldsCreationTest( String path,
			Consumer< Request > setter, Pair< Function< Entity, Object >,  Function< Entity, Object > > valueProvider ) {
		Entity entity = getNewEntity();
		Request request = convertToRequest( entity );
		setter.accept( request );
		
		try {
			mvc.perform( post( path, entity.getId() )
					.contentType( MediaType.APPLICATION_JSON )
					.content( getJson( request ) ) )
				.andExpect( status().is2xxSuccessful() );
		} catch( Exception e ) {
			LOGGER.error( "Failed OptionalFieldsCreation test because of an exception", e );
			fail();
		}
		
		Entity result = resultCaptor.getResult();
		assertEquals( valueProvider.getRight().apply( result ), valueProvider.getLeft().apply( result ) );
	}
	
	@Test
	public void testItemUpdating() throws Exception {
		Entity entity = getNewEntity();
		
		Entity entityWithNewValues = getNewEntity();
		entityWithNewValues.setId( entity.getId() );
		
		Request request = convertToRequest( entityWithNewValues );
		
		List< Function< Entity, Object >  > valuesGetters = getValueToBeUpdated(  request );

		mockServiceGetEntity( entity );
		
		mockServiceCreateOrUpdateMethod( resultCaptor, whenUpdateInService( any( getEntityClass() ) ) );
		
		ResultActions resultAction = mvc.perform( put( getControllerPath() + "/{id}", entity.getId() )
				.contentType( MediaType.APPLICATION_JSON )
				.content( getJson( request ) ) )
			.andExpect( status().isOk() );
		
		Entity updatedEntity = resultCaptor.getResult();
		
		Response expectedResponse = convertToResponse( updatedEntity );
		resultAction.andExpect( content().json( getJson( expectedResponse ) ) );
		
		for ( Function< Entity, Object > getter : valuesGetters ) {
			assertEquals( getter.apply( entityWithNewValues ), getter.apply( updatedEntity ) );
		}
		
	}
	
	@Test
	public void testPatching() throws Exception {
		String path = getControllerPath() + "/{id}";
		getPatchValuesTestParameters().forEach( ( requestedMap, valueProvider ) -> doPatchTest( path, requestedMap, valueProvider ) );
	}
	
	private void doPatchTest( String path, Map< String, Object > requestedMap, Pair< Function< Entity, Object >, Object > valueProvider ) {
		
		Entity entity = getNewEntity();
		
		mockServiceGetEntity( entity );
		mockServiceCreateOrUpdateMethod( resultCaptor, whenUpdateInService( any( getEntityClass() ) ) );
		
		try {
			mvc.perform( patch( path, entity.getId() )
					.contentType( MediaType.APPLICATION_JSON )
					.content( getJson( requestedMap ) ) )
				.andExpect( status().isOk() );
		} catch( Exception e ) {
			LOGGER.error( "Failed patch test because of an exception", e );
			fail();
		}
		Entity updatedEntity = resultCaptor.getResult();
		assertEquals( valueProvider.getRight(), valueProvider.getLeft().apply( updatedEntity ) );
	}
	
	@Test
	public void testPatchingExceptions() throws Exception {
		String path = getControllerPath() + "/{id}";
		getPatchExceptionsValuesTestParameters().forEach( ( requestedMap, reasonMessage ) -> doPatchExceptionTest( path, requestedMap, reasonMessage ) );		
	}
	
	private void doPatchExceptionTest( String path, Map< String, Supplier< Object > > requestedMap, String reasonMessage ) {
		Entity entity = getNewEntity();
		mockServiceGetEntity( entity );
		
		Map< String, Object > queryMap = new HashMap<>();
		for( Entry< String, Supplier< Object > > entry : requestedMap.entrySet() ) {
			queryMap.put( entry.getKey(), entry.getValue().get() );
		}
		
		try {
			mvc.perform( patch( path, entity.getId() )
					.contentType( MediaType.APPLICATION_JSON )
					.content( getJson( queryMap ) ) )
				.andExpect( status().is4xxClientError() )
				.andExpect( jsonPath( "$.title.defaultDescription" ).value( reasonMessage ) );
		} catch( Exception e ) {
			LOGGER.error( "Failed patch test because of an exception", e );
			fail();
		}
	}
	
	@Test
	public void testSuccessOnFindById() throws Exception {
		Entity entity = getNewEntity();

		mockServiceGetEntity( entity );
		
		Response expectedResponse = convertToResponse( entity ); 

		mvc.perform( get( getControllerPath() + "/{id}", entity.getId() )
				.accept( MediaType.APPLICATION_JSON ) )
			.andDo( MockMvcResultHandlers.print() )
			.andExpect( status().isOk() )
			.andExpect( content().json( getJson( expectedResponse ) ) );
	}

	@Test
	public void testNotFoundOnFindById() throws Exception {
		Long missingId = 10000L;

		when( getService().getOne( anyLong() ) ).thenReturn( null );

		ObjectNotFoundException notFoundException = LocalizedExceptionUtil
				.buildObjectNotFoundException( getEntityClass(), missingId);

		mvc.perform( get( getControllerPath() + "/{id}", missingId )
				.accept( MediaType.APPLICATION_JSON ) )
			.andExpect( status().isNotFound() )
			.andExpect( jsonPath( "$.status" ).value("NOT_FOUND"))
			.andExpect( jsonPath( "$.title.defaultDescription" )
					.value( notFoundException.getLocalizedMessage() )
			);
	}

	
	@Test
	public void testCreatingWithWrongParameter() {
		whenCreateInService( any( getEntityClass() ) ).thenAnswer( invocation -> {
			Entity entity = spy( invocation.getArgument( 0, getEntityClass() ) );
			when( entity.getId() ).thenReturn( getRandomId() );
			return entity;
		});
		
		getCreateWithWrongValuesTestParameters().forEach( this :: doCreatingWithWrongValuesTest );
	}
	
	protected void doCreatingWithWrongValuesTest( Consumer< Request > missingValueSetter, String exceptionMessage ) {
		Request request = getNewRequestBean();
		missingValueSetter.accept( request );
		
		try {
			mvc.perform( post( getControllerPath() )
					.contentType( MediaType.APPLICATION_JSON )
					.content( getJson( request ) ) )
				.andExpect( status().is4xxClientError() )
				.andExpect( jsonPath( "$.title.defaultDescription" ).value( exceptionMessage ) );
		} catch( Exception e  ) {
			LOGGER.error( "Failed creation test because of an exception", e );
			fail();
		}
	}
	
	protected void testServiceExceptionDuringCreation( LocalizedException describedServiceException ) {
		whenCreateInService( any( getEntityClass() ) ).thenThrow( describedServiceException );
		doCreatingWithWrongValuesTest( request -> {}, describedServiceException.getMessage() );
	}
	
	@Test
	public void testUpdatingWithMissingParameter() {
		getUpdateWithWrongValuesTestParameters().forEach( this :: doUpdatingWithMissingValuesTest );
	}
	
	protected void doUpdatingWithMissingValuesTest( Consumer< Request > missingValueSetter, String exceptionMessage ) {
		Entity entity = getNewEntity();
		Request request = convertToRequest( entity );
		missingValueSetter.accept( request );
		
		mockServiceGetEntity( entity );
		
		try {
			mvc.perform( put( getControllerPath() + "/{id}", entity.getId() )
					.contentType( MediaType.APPLICATION_JSON )
					.content( getJson( request ) ) )
				.andExpect( status().is4xxClientError() )
				.andExpect( jsonPath( "$.title.defaultDescription" ).value( exceptionMessage ) );
		} catch( Exception e  ) {
			LOGGER.error( "Failed updating test because of an exception", e );
			fail();
		}
	}
	
	@Test
	public void testFindAll() throws Exception {
		List< Entity > entities = new LinkedList<>( Arrays.asList( getNewEntity(), getNewEntity() ) );

		Page< Entity > page = mockPage( entities );
		
		when( getService().get( any(), any(Pageable.class) ) ).thenReturn(page);
		
		List< Response > expectedResponse = entities.stream().map( this :: convertToResponse ).collect( Collectors.toList() );
 
		PageResponse< Response > expectedResponses = new PageResponse<>(expectedResponse, page.getTotalElements(), page.getNumber(), page.getSize());
		mvc.perform( get( getControllerPath() )
				.accept( MediaType.APPLICATION_JSON_VALUE ) )
			.andExpect( status().isOk() )
			.andExpect( content().json( getJson( expectedResponses ) ) );
	}
	
	@Test
	public void testDefaultPageValuesForFindAll() throws Exception {

		Page< Entity > page = getMockedServicePage();

		when( getService().get( any(), any(Pageable.class) ) ).thenReturn( page );

		mvc.perform(get( getControllerPath())
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.param("index", "1"))
				.andExpect(status().isOk());

		assertEquals( 25, getLastPageRequest().getPageSize());
	}

	@Test
	public void testCustomPageValuesForFindAll() throws Exception {

		Page< Entity > page = getMockedServicePage();

		when( getService().get( any(), any(Pageable.class) ) ).thenReturn( page );

		mvc.perform(get( getControllerPath())
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.param("index", "8")
				.param("size", "52"))
				.andExpect(status().isOk());

		Pageable lastPageRequest = getLastPageRequest();
		assertEquals( 52, lastPageRequest.getPageSize());
		assertEquals( 8, lastPageRequest.getPageNumber());
	}
	
	@Test
	public void testPageSizeValidationForFindAll() throws Exception {
		mvc.perform( get( getControllerPath() )
				.param( "size", "101" )
				.accept( MediaType.APPLICATION_JSON_VALUE ) )
			.andExpect( status().is4xxClientError() )
			.andExpect( jsonPath( "$.title.defaultDescription" ).value( "Validation failed, Page size must be between 1 and 100 inclusive" ) );
		
		mvc.perform( get( getControllerPath() )
				.param( "size", "-1" )
				.accept( MediaType.APPLICATION_JSON_VALUE ) )
			.andExpect( status().is4xxClientError() )
			.andExpect( jsonPath( "$.title.defaultDescription" ).value( "Validation failed, Page size must be between 1 and 100 inclusive" ) );
	}
	
	@Test
	public void testPageIndexValidationForFindAll() throws Exception {
		mvc.perform( get( getControllerPath() )
				.param( "index", "-1" )
				.accept( MediaType.APPLICATION_JSON_VALUE ) )
			.andExpect( status().is4xxClientError() )
			.andExpect( jsonPath( "$.title.defaultDescription" ).value( "Validation failed, Page index must have a positive or zero value" ) );
	}

	@Test
	public void findAllSortingTest() {
		Page< Entity > page = getMockedServicePage();

		when( getService().get( any(), any(Pageable.class) ) ).thenReturn(page);

		Map< List< String >, Sort > sortingTestParameters = getSortingTestParameters();
		sortingTestParameters.forEach( this :: doSortingTest );
	}

	protected void doSortingTest( List< String > sortingValues, Sort expectedResult ) {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get( getControllerPath() );
		sortingValues.forEach( sortingValue -> mockHttpServletRequestBuilder.param( "sort", sortingValue  ) );
		try {
			mvc.perform( mockHttpServletRequestBuilder )
					.andExpect( status().isOk() );
		} catch( Exception e ) {
			LOGGER.error( "Failed sorting test because of an exception", e );
			fail();
		}
		assertEquals( expectedResult, getLastPageRequest().getSort() );
	}
	
	@Test
	public void testFilteringForFindAll() {
		Page< Entity > page = getMockedServicePage();

		when( getService().get( any(), any(Pageable.class) ) ).thenReturn( page );

		Map< List< String >, List< SearchCriteria > > filteringTestParameters = getFilteringTestParameters();
		filteringTestParameters.forEach( this :: doFilterTest );
	}
	
	@SuppressWarnings("unchecked")
	protected void doFilterTest( List< String > filteringValues,  List< SearchCriteria >  expectedSearchCriterias ) {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get( getControllerPath() );
		filteringValues.forEach( filteringValue -> mockHttpServletRequestBuilder.param( "search", filteringValue ) );

		ArrayList< SearchCriteria > listClassObjsct = new ArrayList<>();
		ArgumentCaptor< List< SearchCriteria > > createriasCaptor = ArgumentCaptor.forClass( listClassObjsct.getClass() );
		try {
			mvc.perform( mockHttpServletRequestBuilder )
					.andExpect( status().isOk() );
		} catch( Exception e ) {
			LOGGER.error( "Failed filtering test because of an exception", e );
			fail();
		}

		verify( getSpecificationsBuilder(), atLeastOnce() ).buildSpecification( createriasCaptor.capture() );
		Specification< Entity > specification = getLastCalledSpecification();
		assertNotNull( specification, expectedSearchCriterias.toString() );
		assertEquals( expectedSearchCriterias,  createriasCaptor.getValue() );
	}
	
	@Test
	public void findAllFilteringWrongValueTest() throws Exception {
		Page< Entity > page = getMockedServicePage();

		when( getService().get( any(), any(Pageable.class) ) ).thenReturn( page );


		mvc.perform( get( getControllerPath() )
				.param( "search", "bla bla" ) )
            .andExpect( status().isOk() );
		assertNull( getLastCalledSpecification() );
	}

	@Test
	public void testDelete() throws Exception {
		Entity entity = getNewEntity();
		
		mockServiceGetEntity( entity );
		
		mvc.perform( delete( getControllerPath() + "/{id}", entity.getId() ) )
			.andExpect( status().isNoContent() );

		verify( getService(), times( 1 ) ).delete( eq( entity.getId() ) );
	}
	
	@Test
	public void testCanNotDelete() throws Exception {
		Entity entity = getNewEntity();
		
		mockServiceGetEntity( entity );
		
		Mockito.doThrow(RemovalNotAllowedException.class).when(getService()).delete( entity.getId() );

		mvc.perform( delete( getControllerPath() + "/{id}", entity.getId() ) ).andDo( MockMvcResultHandlers.print() )
				.andExpect( status().isNotAcceptable() );
	}

	@Test
	public void testCanNotUpdate() throws Exception {
		Entity entity = getNewEntity();
		Request request = convertToRequest( entity );

		mockServiceGetEntity( entity );

		whenUpdateInService( any( getEntityClass() ) ).thenThrow( UpdateNotAllowedException.class );

		mvc.perform( put( getControllerPath() + "/{id}", entity.getId() )
						.contentType( MediaType.APPLICATION_JSON )
						.content( getJson( request ) ) )
				.andExpect( status().isMethodNotAllowed() );
	}
	
	protected OngoingStubbing< Entity > whenCreateInService( Entity entity ) {
		return when( getService().create( entity ) );
	}
	
	protected OngoingStubbing< Entity > whenUpdateInService( Entity entity ) {
		return when( getService().update( entity ) );
	}
	
	protected void mockServiceGetEntity( Entity entity ) {
		when( getService().getOne( entity.getId() ) ).thenReturn( entity );
	}
	
	protected abstract Class< Entity > getEntityClass();
	
	protected abstract AbstractService< Entity > getService();
	
	protected abstract Entity getNewEntity();
	
	protected abstract Request getNewRequestBean();
	
	protected abstract Map< List< String >, Sort> getSortingTestParameters();
	
	/* TODO: Is the parameter needed here? */
	protected abstract List< Function< Entity, Object > > getValueToBeUpdated( Request request );

	protected abstract Map< List< String >, List< SearchCriteria > > getFilteringTestParameters();
	
	protected abstract EntityFilterSpecificationsBuilder<Entity> getSpecificationsBuilder();
	
	protected abstract HashMap< Consumer< Request >, Pair< Function< Entity, Object >, Function< Entity, Object > > > getCreateOptionalFieldsTestParameters();
	
	protected abstract Map< Map< String, Supplier< Object > >, String > getPatchExceptionsValuesTestParameters();
	
	protected abstract Map< Map< String, Object >, Pair< Function< Entity, Object >, Object > > getPatchValuesTestParameters();
	
	protected abstract Request convertToRequest( Entity entity );
	
	protected abstract Response convertToResponse( Entity entity );
	
	protected abstract Map< Consumer< Request >, String > getCreateWithWrongValuesTestParameters();
	
	protected Map< Consumer< Request >, String > getUpdateWithWrongValuesTestParameters(){
		return getCreateWithWrongValuesTestParameters();
	}
	
	protected abstract String getControllerPath();
	
	protected String getControllerImportPath() {
		return getControllerPath() + "/import";
	}
	
	protected Pageable getLastPageRequest() {
		ArgumentCaptor< Pageable > captor = ArgumentCaptor.forClass(Pageable.class);
		verify( getService(), atLeastOnce() ).get( any(), captor.capture() ) ;
		return captor.getValue();
	}
	
	@SuppressWarnings("unchecked")
	protected Specification< Entity > getLastCalledSpecification() {
		ArgumentCaptor< Specification< Entity > > captor = ArgumentCaptor.forClass( Specification.class );
		verify( getService(), atLeastOnce() ).get( captor.capture(), any( Pageable.class ) );
		return captor.getValue();
	}
	
	protected Entity getLastCreatedEntity() {
		ArgumentCaptor< Entity > captor = ArgumentCaptor.forClass( getEntityClass() );
		whenCreateInService( captor.capture() );
		return captor.getValue();
	}
	
	protected Entity getLastUpdatedEntity() {
		ArgumentCaptor< Entity > captor = ArgumentCaptor.forClass( getEntityClass() );
		whenUpdateInService( captor.capture() );
		return captor.getValue();
	}
 
	private Page< Entity > getMockedServicePage() {
		return mockPage( new ArrayList<>() );
	}
	
	@SuppressWarnings("unchecked")
	private Page< Entity > mockPage( List< Entity > entities ) {
		Page< Entity > page = mock(Page.class);
		when( page.getContent()).thenReturn(entities);
		when( page.getTotalElements() ).thenReturn( 765L );
		when( page.getNumber() ).thenReturn( 1 );
		when( page.getSize() ).thenReturn( 5 );
		when( page.iterator() ).thenReturn( entities.iterator() );

		when( getService().get( any(), any(Pageable.class) ) ).thenReturn(page);
		return page;
	}
	
	protected Long getRandomId() {
		return ThreadLocalRandom.current().nextLong( Long.MAX_VALUE - 1 );
	}
}
