package com.voltor.futureleave.dao.specification;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.model.Identifiable;

public class InSpecification<T extends Identifiable, R extends Serializable> implements Specification<T> {

	private static final long serialVersionUID = 7652231116221930648L;

	private final String fieldPath;

	private final Collection< R > values;

	public InSpecification( String fieldPath, Collection< R > values ) {
		this.fieldPath = fieldPath;
		this.values = values;
	}

	@Override
	public Predicate toPredicate( Root< T > root, CriteriaQuery< ? > cq, CriteriaBuilder cb ) {
		Path< T > path = SpecificationUtil.buildPath( root, fieldPath );
		return path.in( values );
	}

}
