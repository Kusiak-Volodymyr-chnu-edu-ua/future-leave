package com.voltor.futureleave.filtering.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class StringComparisonSpecification< EntityType > implements Specification< EntityType > {
 
	private static final long serialVersionUID = 4568825149040657679L;
	
	private final SearchCriteria searchCriteria;

	public StringComparisonSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate( Root< EntityType > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		
		String searchValueLowerCase = StringUtils.lowerCase( (String) searchCriteria.getValue() );
		Expression< String > searchExpression = cb.lower( root.get( searchCriteria.getKey() ) );
		
		switch ( searchCriteria.getOperation()  ) {
    		case EQUAL:
    			return cb.equal( searchExpression, searchValueLowerCase );
    		case NOT_EQUAL:
    			return cb.notEqual( searchExpression, searchValueLowerCase );
    		case CONTAIN:
    			return cb.like( searchExpression, "%" + searchValueLowerCase + "%" );
    		default:
    			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof StringComparisonSpecification)) return false;

		StringComparisonSpecification< ? > that = (StringComparisonSpecification< ? >) o;

		return new EqualsBuilder()
				.append(searchCriteria, that.searchCriteria)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(searchCriteria)
				.toHashCode();
	}

	@Override
	public String toString() {
		return "StringComparisonSpecification{" +
				"searchCriteria=" + searchCriteria +
				'}';
	}
}
