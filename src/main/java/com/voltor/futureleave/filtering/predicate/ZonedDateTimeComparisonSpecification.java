package com.voltor.futureleave.filtering.predicate;

import java.time.ZonedDateTime;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class ZonedDateTimeComparisonSpecification< EntityType > implements Specification<EntityType> {

	private static final long serialVersionUID = -8876125803915202920L;
	
	private final SearchCriteria searchCriteria;

	public ZonedDateTimeComparisonSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		
		ZonedDateTime value = ZonedDateTime.parse(searchCriteria.getValue().toString());
		Expression< ZonedDateTime > searchExpression = root.get( searchCriteria.getKey() );
		
		switch ( searchCriteria.getOperation()  ) {
    		case GREATER_THEN:
				return cb.greaterThan( searchExpression, value );
    		case LESS_THEN:
    			return cb.lessThan( searchExpression, value );
    		case GREATER_OR_EQUAL:
    			return cb.greaterThanOrEqualTo( searchExpression, value);
    		case LESS_OR_EQUAL:
    			return cb.lessThanOrEqualTo( searchExpression, value );
    		case EQUAL:
    			return cb.equal( searchExpression, value );
    		case NOT_EQUAL:
    			return cb.notEqual( searchExpression, value );
    		default:
    			return null;
		}
	}
}
