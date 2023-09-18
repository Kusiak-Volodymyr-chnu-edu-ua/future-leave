package com.voltor.futureleave.filtering.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class EqualingSpecification< EntityType > implements Specification< EntityType > {

	private static final long serialVersionUID = 800528251296820234L;

	private final SearchCriteria searchCriteria;

	public EqualingSpecification( SearchCriteria searchCriteria ) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate( Root< EntityType > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		switch ( searchCriteria.getOperation()  ) {
    		case EQUAL:
    			return cb.equal( root.get( searchCriteria.getKey() ), searchCriteria.getValue()  );
    		case NOT_EQUAL:
    			return cb.notEqual( root.get( searchCriteria.getKey() ), searchCriteria.getValue()  );
    		default:
    			return null;
    	}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof EqualingSpecification)) return false;

		EqualingSpecification< ? > that = (EqualingSpecification< ? >) o;

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
		return "EqualingSpecification{" +
				"searchCriteria=" + searchCriteria +
				'}';
	}
}
