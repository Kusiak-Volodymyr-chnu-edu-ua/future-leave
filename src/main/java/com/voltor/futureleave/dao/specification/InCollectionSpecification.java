package com.voltor.futureleave.dao.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.model.Identifiable;

public class InCollectionSpecification<T, R extends Identifiable> implements Specification<T> {

	private static final long serialVersionUID = 3657277566518014718L;

	private final Class<T> entityClass;

	private final String fieldPath;

	private final R value;

	public InCollectionSpecification( Class< T > entityClass, String fieldPath, R value ) {
		this.entityClass = entityClass;
		this.fieldPath = fieldPath;
		this.value = value;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {

		Subquery< T > subQuery = query.subquery( entityClass );
		Root< T > sqRoot = subQuery.from( entityClass );
		Join< R, T > sqJoin = SpecificationUtil.buildJoin( sqRoot, fieldPath );
		subQuery.select( sqRoot );
		subQuery.where( cb.equal( sqJoin, value ) );

		return cb.in( root ).value( subQuery );
	}

}
