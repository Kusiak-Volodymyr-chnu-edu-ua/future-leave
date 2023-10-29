package com.voltor.futureleave.api.v1.session;

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
import com.voltor.futureleave.filtering.predicate.SessionSpecificationBuilder;
import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.service.AbstractService;
import com.voltor.futureleave.service.SessionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

@Tag( name = "Session" )
@RestController
@RequestMapping( ApiConstants.V1_SESSION_ENDPOINT )
public class SessionController extends AbstractController<Session, SessionRequest, SessionResponse>  {
	
	@Autowired
	private SessionSpecificationBuilder specificationBuilder;
	
	@Autowired
	private SessionService service;
	 
	private Validator validator;
	
	public SessionController( ValidatorFactory validatorFactory ) {
		validator = validatorFactory.getValidator();
	} 
	
	public SessionResponse create() {
		return null;
	}

	@Override
	public AbstractService<Session> getService() {
		return service;
	}

	@Override
	public Class<Session> getEntityClass() {
		return Session.class;
	}

	@Override
	public SessionResponse convertEntityToResponse( Session session, List<String> entitiesToExpand ) {
		SessionResponse sessionResponse = new SessionResponse( session );
		sessionResponse.setName( session.getName() );
		return sessionResponse;
	}
	
	@Override
	public EntityFilterSpecificationsBuilder<Session> getFilterSpecificationsBuilder() {
		return specificationBuilder;
	} 
	
	
	@Override
	protected void patchFields( Session entity, Map< String, Object > request ) {
		request.entrySet().forEach( requestedField -> patchField( entity, requestedField ) );
	}
	
	protected void patchField( Session entity, Entry< String, Object > requestedField ) {
		switch( requestedField.getKey() ) {
			case "name":
				String name = conversionService.convert( requestedField.getValue(), String.class );
				validatePatchField( "name", name );
				entity.setName( name );
				return;
    		default:
    			return;
    	}
	}
	
	private void validatePatchField( String propertyName, Object value ) {
		Set< ConstraintViolation< SessionRequest > > validationResult = validator
				.validateValue( SessionRequest.class, propertyName, value, UpdateValidationGroup.class, Default.class );
		if(validationResult.isEmpty()) {
			return;
		}
		throw new PatchFieldConstraintViolationException( validationResult );
	}

}