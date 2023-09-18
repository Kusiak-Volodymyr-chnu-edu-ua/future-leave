package com.voltor.futureleave.filtering.session;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.model.PrimaryEntity;

public class IdSpecification< IdType, EntityType extends PrimaryEntity< IdType > >
		implements Specification< EntityType > {
 
	private static final long serialVersionUID = -3617213012291647979L;
	
	final IdType id;
	boolean inverse = false;

	public IdSpecification( IdType id ) {
		this.id = id;
	}

	public IdSpecification( IdType id, boolean inverse ) {
		this.id = id;
		this.inverse = inverse;
	}

	@Override
	public Predicate toPredicate( Root< EntityType > root, CriteriaQuery< ? > query, CriteriaBuilder cb ) {
		if ( !inverse ) {
			return cb.equal( root.get( "id" ), ( id ) );
		} 
		return cb.notEqual( root.get( "id" ), ( id ) );
	}
}
