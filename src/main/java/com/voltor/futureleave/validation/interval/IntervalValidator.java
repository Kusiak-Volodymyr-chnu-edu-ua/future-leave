package com.voltor.futureleave.validation.interval;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IntervalValidator implements ConstraintValidator<ValidInterval, Object> {

    public void initialize(ValidInterval constraintAnnotation) {

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public boolean isValid(Object model, ConstraintValidatorContext constraintValidatorContext) {
    	Field fromField = null;
    	Comparable< Object > from = null;
    	Field tillField = null;
    	Comparable< Object > till = null;
    	try {
        	for( Field field : model.getClass().getDeclaredFields() ) {
        		
        		if( field.isAnnotationPresent( IntervalFrom.class  ) ) {
        			fromField = field;
        			fromField.setAccessible(true);
        			Object fieldValue = fromField.get( model );
    				 
        			if( fieldValue instanceof Comparable ){
        				from = (Comparable) fieldValue;
        			}
        		}
        		
        		if( field.isAnnotationPresent( IntervalTill.class  ) ) {
        			tillField = field;
        			tillField.setAccessible(true);
        			Object fieldValue = tillField.get( model );
    				 
        			if( fieldValue instanceof Comparable ){
        				till = (Comparable) fieldValue;
        			}
        		}
        		
        	}
    	} catch ( IllegalArgumentException | IllegalAccessException e ) {
    		return true;
		}
    	if( from != null && till!=null && from.compareTo( till ) > 0 ) {
    		String message = fromField.getName() + " must not be after " + tillField.getName();
    		setExceptionMessage( message, constraintValidatorContext );
    		return false;
    	}
    	
    	return true;
    }
    
    private void setExceptionMessage( String message, ConstraintValidatorContext constraintContext ) {
    	constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate( message )
	        .addConstraintViolation();		
	}
}