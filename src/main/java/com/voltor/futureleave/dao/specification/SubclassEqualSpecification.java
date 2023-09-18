package com.voltor.futureleave.dao.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class SubclassEqualSpecification<T, S extends T> implements Specification<T> {

	private static final long serialVersionUID = -1506428092338995164L;

	private final Class< S > subclassClass;

	private final String fieldPath;

	private final Object value;

	public SubclassEqualSpecification( String fieldPath, Object value, Class< S > subclassClass ) {
		this.fieldPath = fieldPath;
		this.value = value;
		this.subclassClass = subclassClass;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		// A subquery is used because this should work with all InheritanceType
		// options (SINGLE_TABLE, TABLE_PER_CLASS, and JOINED).
		Subquery< S > subQuery = query.subquery( subclassClass );
		Root< S > sqRoot = subQuery.from( subclassClass );
		subQuery.select( sqRoot );
		Path< S > path = SpecificationUtil.buildPath( sqRoot, fieldPath );
		subQuery.where( cb.equal( path, value ) );

		return cb.in( root ).value( subQuery );
	}

}
