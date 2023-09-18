package com.voltor.futureleave.filtering.predicate;

import java.util.Collections;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.filtering.session.SessionSpecification;
import com.voltor.futureleave.model.Session;

public class SessionSpecificationBuilder implements SpecificationBuilder< Session > {

	public static final List< FilteringOperation > SUPPORTED_OPERATORS = Collections
			.singletonList( FilteringOperation.EQUAL );

	@Override
	public Specification< Session > buildSpecification( SearchCriteria searchCriteria ) {
		return new SessionSpecification<>( (Long) searchCriteria.getValue() );
	}
}
