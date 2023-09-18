package com.voltor.futureleave.dao.specification;

import java.util.Objects;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class GreaterThenSpecification<T, V extends Comparable<V> > implements Specification<T> {
 
	private static final long serialVersionUID = -2550145329349526320L;

	private final String fieldPath;

	private final V value;

	public GreaterThenSpecification( String fieldPath, V value ) {
		this.fieldPath = fieldPath;
		this.value = value;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		Expression< V > searchExpression = root.get( fieldPath );
		return cb.greaterThan( searchExpression, value );
	}

	@Override
	public int hashCode() {
		return Objects.hash( fieldPath, value );
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		GreaterThenSpecification other = (GreaterThenSpecification) obj;
		return Objects.equals( fieldPath, other.fieldPath ) && Objects.equals( value, other.value );
	}

}
