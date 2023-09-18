package com.voltor.futureleave.filtering.session;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ArchivedSpecification< EntityType > implements Specification< EntityType > {

	private static final long serialVersionUID = -7315580228476708139L;

	@Override
	public Predicate toPredicate( Root< EntityType > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		return cb.equal( root.get( "archived" ), false );
	}
}