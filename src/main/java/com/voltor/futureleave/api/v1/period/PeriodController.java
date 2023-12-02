package com.voltor.futureleave.api.v1.period;

import java.time.LocalDate;
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
import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.filtering.predicate.PeriodSpecificationBuilder;
import com.voltor.futureleave.model.Period;
import com.voltor.futureleave.service.AbstractService;
import com.voltor.futureleave.service.PeriodService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

@Tag( name = "Period" )
@RestController
@RequestMapping( ApiConstants.V1_PERIOD_ENDPOINT )
public class PeriodController extends AbstractController<Period, PeriodRequest, PeriodResponse>  {
	
	@Autowired
	private PeriodSpecificationBuilder specificationBuilder;
	
	@Autowired
	private PeriodService service;
	 
	private Validator validator;
	
	public PeriodController( ValidatorFactory validatorFactory ) {
		validator = validatorFactory.getValidator();
	}  
	
	@Override
	public AbstractService<Period> getService() {
		return service;
	}

	@Override
	public Class<Period> getEntityClass() {
		return Period.class;
	}

	@Override
	public PeriodResponse convertEntityToResponse( Period period, List<String> entitiesToExpand ) {
		PeriodResponse periodResponse = new PeriodResponse( period );
		periodResponse.setSessionId( period.getSessionId() );
		periodResponse.setStartDate( period.getStartDate() );
		periodResponse.setEndDate( period.getEndDate() );
		periodResponse.setPlannedEventsCount( period.getPlannedEventsCount() );
		periodResponse.setUnplannedEventsCount( period.getUnplannedEventsCount() );
		return periodResponse;
	}
	
	@Override
	public EntityFilterSpecificationsBuilder<Period> getFilterSpecificationsBuilder() {
		return specificationBuilder;
	}  
	
	@Override
	protected void patchFields( Period entity, Map< String, Object > request ) {
		request.entrySet().forEach( requestedField -> patchField( entity, requestedField ) );
	}
	
	protected void patchField( Period entity, Entry< String, Object > requestedField ) {
		switch( requestedField.getKey() ) {
			case "startDate":
				LocalDate startDate = conversionService.convert( requestedField.getValue(), LocalDate.class );
				validatePatchField( "startDate", startDate );
				entity.setStartDate(startDate);
				return;
			case "endDate":
				LocalDate endDate = conversionService.convert( requestedField.getValue(), LocalDate.class );
				validatePatchField( "endDate", endDate );
				entity.setEndDate(endDate);
				return;
			case "plannedEventsCount":
				Integer plannedEventsCount = conversionService.convert( requestedField.getValue(), Integer.class );
				validatePatchField( "plannedEventsCount", plannedEventsCount );
				entity.setPlannedEventsCount(plannedEventsCount);
				return;
			case "unplannedEventsCount":
				Integer unplannedEventsCount = conversionService.convert( requestedField.getValue(), Integer.class );
				validatePatchField( "unplannedEventsCount", unplannedEventsCount );
				entity.setUnplannedEventsCount(unplannedEventsCount);
				return;
    		default:
    			return;
    	}
	}
	
	private void validatePatchField( String propertyName, Object value ) {
		Set< ConstraintViolation< PeriodRequest > > validationResult = validator
				.validateValue( PeriodRequest.class, propertyName, value, UpdateValidationGroup.class, Default.class );
		if(validationResult.isEmpty()) {
			return;
		}
		throw new PatchFieldConstraintViolationException( validationResult );
	}

}