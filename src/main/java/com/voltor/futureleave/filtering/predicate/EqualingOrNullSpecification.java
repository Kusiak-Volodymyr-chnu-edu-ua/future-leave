package com.voltor.futureleave.filtering.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class EqualingOrNullSpecification< T > implements Specification< T > {

	private static final long serialVersionUID = 2206438670684039538L;

	private final SearchCriteria searchCriteria;

	public EqualingOrNullSpecification( SearchCriteria searchCriteria ) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		final String[] parts = searchCriteria.getKey().split( "\\." );
		Path< ? > path;
		if ( parts.length == 2 ) {
			path = root.get( parts[0] ).get( parts[1] );
		} else {
			path = root.get( searchCriteria.getKey() );
		}

		if ( searchCriteria.getValue() != null ) {
			if ( FilteringOperation.EQUAL == searchCriteria.getOperation() ) {
				return cb.equal( path, searchCriteria.getValue() );
			} else if ( FilteringOperation.NOT_EQUAL == searchCriteria.getOperation() ) {
				return cb.notEqual( path, searchCriteria.getValue() );
			} else {
				return null;
			}
		} else {
			if ( FilteringOperation.EQUAL == searchCriteria.getOperation() ) {
				return cb.isNull( path );
			} else {
				return cb.isNotNull( path );
			}
		}
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;

		if ( !( o instanceof EqualingOrNullSpecification ) )
			return false;

		EqualingOrNullSpecification< ? > that = (EqualingOrNullSpecification< ? >) o;

		return new EqualsBuilder().append( searchCriteria, that.searchCriteria ).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append( searchCriteria ).toHashCode();
	}

	@Override
	public String toString() {
		return "EqualingOrNullSpecification{" + "searchCriteria=" + searchCriteria + '}';
	}
}
