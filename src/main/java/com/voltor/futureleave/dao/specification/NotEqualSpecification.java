package com.voltor.futureleave.dao.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.model.Identifiable;

@SuppressWarnings("serial")
public class NotEqualSpecification< T extends Identifiable > implements Specification< T > {

	private final String fieldPath;

	private final Object value;

	public NotEqualSpecification( String fieldPath, Object value ) {
		this.fieldPath = fieldPath;
		this.value = value;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		Path< T > path = SpecificationUtil.buildPath( root, fieldPath );
		return cb.notEqual( path, value );
	}

}
