package com.voltor.futureleave.api.v1.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.voltor.futureleave.api.v1.BaseControllerUtil;
import com.voltor.futureleave.api.v1.exception.HttpRequestMethodNotSupportedException;
import com.voltor.futureleave.api.v1.exception.LocalizedExceptionUtil;
import com.voltor.futureleave.filtering.FilterableProperty;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.IllegalFilteringOperationException;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.service.AbstractService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.Default;
import jakarta.websocket.server.PathParam;

@Validated
@SecurityRequirement(name = "Bearer Authentication")
public abstract class AbstractController<
	T extends Identifiable, 
	RequestType extends AbstractRequest<T>, 
	ApiResponseType extends AbstractResponse>
		implements FilterableController<T> {
	
	public static final Integer DEFAULT_PAGE_SIZE = 25;
	public static final Integer DEFAULT_PAGE_INDEX = 0;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
	private static final Pattern PATTERN = Pattern.compile("(\\w+?)(:|!_=|[!<>_]=?|=)(.*)");

	public abstract AbstractService<T> getService();

	public abstract ApiResponseType convertEntityToResponse(T entity, List<String> entitiesToExpand);
	
	@Autowired
	protected ConversionService conversionService;
	
	public abstract Class<T> getEntityClass();

	@Operation(summary = "Retrieve a list of all records")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity< PageResponse< ApiResponseType > >getAll(
			
			@Parameter( description = "Page number of the requested page. The default value is 0", example = "0" )
			@RequestParam( required = false ) 
			Optional< @PositiveOrZero( message = "Page index must have a positive or zero value" ) Integer> index,
			
			@Parameter( description = "The size of the requested page. The default value is 25. Maximum size is 100", example = "25")
			@RequestParam( required = false ) 
			Optional< @Range( min = 1, max = 100, message = "Page size must be between 1 and 100 inclusive" ) Integer > size,
			
			@Parameter( description = "Search expression used to filter results. "
					+ "To check fields which support filtering, please reffer to the response model",
					example = "field1=value1,field2!=value2,field3:value3" )
			@RequestParam( required = false ) Optional<String> search,
			
			@Parameter( description = "Specify fields to sort. The default sorting is ascending" ) 
			@PathParam( "sort" ) Sort sort,
			
			@Parameter(name = "expand_fields", description = "Specify fields which have to be expanded in the response. "
						+ "To check fields which support expanding, please refer to the response model",
						example = "field1,field2" )
			@RequestParam(value = "expand_fields", required = false) Optional<String> expand ) {
		int pageSize = size.orElse( DEFAULT_PAGE_SIZE );
		Specification<T> filteringSpecification = buildDefaultGetAllFilteringSpec(search);
		sort = mapSortPropertities( sort );

		int pageIndex = index.orElse( DEFAULT_PAGE_INDEX );

		Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
		Page<T> entitiesPaged = getService().get(filteringSpecification, pageable);
		List<ApiResponseType> responses = entitiesPaged.getContent().stream()
				.map( item -> convertEntityToResponse(item, BaseControllerUtil.parseExpandField(expand))).collect(Collectors.toList());
		PageResponse< ApiResponseType > apiResponseTypePageResponse =
				new PageResponse<>(responses, entitiesPaged.getTotalElements(), entitiesPaged.getNumber(), entitiesPaged.getSize());
		return ResponseEntity.ok(apiResponseTypePageResponse);
	}
	

	protected Sort mapSortPropertities( Sort sort ) {
		if ( sort == null ) {
			return Sort.unsorted();
		}
		Iterator< Order > iterator = sort.iterator();
		List< Order > mappedOrders = new LinkedList<>();
		while ( iterator.hasNext() ) {
			Order order = iterator.next();
			Order mappedOrder = new Order( 
					order.getDirection(), 
					mapDBProperty( order.getProperty() ),
					order.getNullHandling() );
			mappedOrders.add( mappedOrder );
		}
		return Sort.by( mappedOrders );
	}

	protected String mapDBProperty( String property) {
		switch ( property ) {
    		case "bcId":
    			return "externalId";
    		default:
    			return property;
		}
		
	}

	@GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Retrieve a specific record by id",
			description = "This operation retrieves specific record of current entity from database\n" +
					"- <b>expand_fields</b> - is used to specify the entity fields that need to be included in the response.\n" +
					"- <b>id</b> - is used to specify the identification number of record which must be returned.")
	@ResponseBody
	public ApiResponseType getRecord(@PathVariable(value = "id") Long id,
									 @Parameter(name = "expand_fields", description = "Specify fields which has to be expanded " +
											 "in response. To check fields which support expanding, please refer to response model")
									 @RequestParam(value = "expand_fields", required = false) Optional<String> expand,
									 HttpServletResponse response,
									 HttpServletRequest servletRequest) {
		T entity = getService().getOne( id  );
		BaseControllerUtil.getObjectOrNotFound( entity, id, getEntityClass() );
		return convertEntityToResponse( entity, BaseControllerUtil.parseExpandField( expand ) );
	}

	@PostMapping(
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Create record")
	@ResponseBody
	@ResponseStatus( HttpStatus.CREATED )
	public ApiResponseType createRecord(@Validated({CreateValidationGroup.class, Default.class}) @RequestBody RequestType request,
										HttpServletResponse response) {
		
		/* TODO: Why the create action is the responsibility of the request object? */
		T entity = request.createEntity();

		entity = executeEntityCreate(entity, request);

		return convertEntityToResponse(entity, expandFieldsOnCreateAndUpdate());
	}

	@PutMapping(path = "/{id}",
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Update a specific record, based on the id", 
		description = "Update a specific record, based on the id<br>"
				+ "All values will be replaced by the input data")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "404", description = "The record to be updated does not exist."),
			@ApiResponse(responseCode = "406", description = "Invalid property value")
	})
	@ResponseBody
	@ResponseStatus( HttpStatus.OK )
	public ApiResponseType updateRecord(@PathVariable(value = "id") Long id,
										@Validated({UpdateValidationGroup.class, Default.class}) @RequestBody RequestType request,
										HttpServletResponse response) {
		T entity = getByID( id );
		
		request.updateEntity( entity );

		entity = executeEntityUpdate( entity, request);
		
		return convertEntityToResponse(entity, expandFieldsOnCreateAndUpdate());
	}

	protected T executeEntityUpdate( T entity, RequestType request) {
		return getService().update(entity);
	}

	/* TODO: is the request needed? */
	protected T executeEntityCreate(T entity, RequestType request) {
		return getService().create(entity);
	}
	
	protected T executeEntityPatch( T entity, Map<String, Object> request) {
		return getService().update(entity);
	}
	
	@PatchMapping(path = "/{id}",
			consumes = {MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Patch a specific record, based on the id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "404", description = "The request was empty"),
			@ApiResponse(responseCode = "404", description = "The record to be patched does not exist."),
			@ApiResponse(responseCode = "405", description = "The PATCH method is not implemented for this endpoint.")
	})
	@ResponseBody
	public ApiResponseType patchRecord(
			@PathVariable(value = "id") Long id, @RequestBody Map<String, Object> request,
			HttpServletResponse response) {
		BaseControllerUtil.checkPatchRequest(request);
		T entity = getByID( id );
		
		patchFields(entity, request);

		entity = executeEntityPatch( entity, request );

		return convertEntityToResponse(entity, Collections.emptyList());
	}

	/**
	 * Method to be overridden by implementing Controller if the Http PATCH method is allowed.
	 * <p>
	 * Abstract implementation throws an unchecked {@link HttpRequestMethodNotSupportedException} which results in Http status code 405 (Method Not Allowed).
	 * It is only allowed to PATCH simple fields, that means no fields that are a reference to a other entity.
	 *
	 * @param entity        The entity in which the updates need to be done.
	 * @param fieldsToPatch A Map of the JSON fields in the request. These values should be updated in the entity.
	 */
	protected void patchFields(T entity, Map<String, Object> fieldsToPatch) {
		throw LocalizedExceptionUtil.buildHttpRequestMethodNotSupportedException("PATCH");
	}

	@Operation( summary = "Delete record by id" )
	@DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRecord(@PathVariable(value = "id") Long id,
							 HttpServletResponse response) {
		T entity = getByID( id );
		getService().delete(entity.getId());
	}
	
	public List<String> expandFieldsOnCreateAndUpdate() {
		return Collections.emptyList();
	}

	protected Specification<T> buildDefaultGetAllFilteringSpec(Optional<String> search) {
		if (search.isPresent() && this.getFilterSpecificationsBuilder() != null) {
			List< FilterableProperty< T > > filterableProperties = this.getFilterSpecificationsBuilder().getFilterableProperties();
			List< SearchCriteria > searchCriteria = parseSearchCriteria( search.get(), filterableProperties );
			return this.getFilterSpecificationsBuilder().buildSpecification(searchCriteria);
		}
		return null;
	}
	
	protected List< SearchCriteria > parseSearchCriteria( String searchQuery, List< FilterableProperty< T > > filterableProperties ) {
		String[] searchParams = searchQuery.split(",");
		List<SearchCriteria> searchCriteria = new ArrayList<>();

		for (String searchParameter : searchParams) {
			Matcher matcher = PATTERN.matcher(searchParameter);
			while (matcher.find()) {
				String key = mapDBProperty( matcher.group(1) );
				String operationStr = matcher.group(2);
				FilteringOperation operation = FilteringOperation.fromString(operationStr);
				String value = matcher.group(3);

				Optional< FilterableProperty< T > > filterableProperty = filterableProperties.stream()
						.filter( property -> property.getPropertyName().equals( key ) ).findFirst();

				if (filterableProperty.isPresent()) {
					
					Object convertedValue;
					if ("null".equals(value) || StringUtils.isEmpty(value)) {
						convertedValue = null;
					} else {
						convertedValue = convertValueForCriteria( key, operation, value, filterableProperty.get() );
					}
					// check if a FilterableOperation is supported
					if (!filterableProperty.get().getOperators().contains(operation)) {
						throw new IllegalFilteringOperationException("Operation '" + operation + "' is not supported for property " + key);
					}
					searchCriteria.add(new SearchCriteria(key, operation, convertedValue));
				} else {
					LOGGER.warn("Filtering on property '{}' has been skipped because it's absent in filterableProperties", key);
				}
			}
		}
		return searchCriteria;
	} 
	
	protected Object convertValueForCriteria( String key, FilteringOperation operation, String value, FilterableProperty< T > filterableProperty ) {
		return conversionService.convert( value, filterableProperty.getExpectedType() );
	} 

	private T getByID( Long id ) {
		return BaseControllerUtil.getObjectOrNotFound(getService().getOne( id ), id, getEntityClass());
	}


}
