package com.voltor.futureleave.dao.specification;

import java.util.Objects;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EqualSpecification<T> implements Specification<T> {
 
	private static final long serialVersionUID = -2550145329349526320L;

	private final String fieldPath;

	private final Object value;

	public EqualSpecification( String fieldPath, Object value ) {
		this.fieldPath = fieldPath;
		this.value = value;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		Path< T > path = SpecificationUtil.buildPath( root, fieldPath );
		return cb.equal( path, value );
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
		EqualSpecification other = (EqualSpecification) obj;
		return Objects.equals( fieldPath, other.fieldPath ) && Objects.equals( value, other.value );
	}

}
