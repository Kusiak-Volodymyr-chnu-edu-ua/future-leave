package com.voltor.futureleave.filtering.predicate;

import java.math.BigDecimal;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class BigDecimalComparisonSpecification<EntityType> implements Specification<EntityType> {

	private static final long serialVersionUID = -4321756743799497958L;
	private final SearchCriteria searchCriteria;

    public BigDecimalComparisonSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    	
        BigDecimal value = (BigDecimal) searchCriteria.getValue();         
		Expression< BigDecimal > searchExpression = root.get( searchCriteria.getKey() );
		
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
