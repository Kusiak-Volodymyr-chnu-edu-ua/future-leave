package com.voltor.futureleave.filtering.predicate;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class StringComparisonSpecificationBuilder< EntityType > implements SpecificationBuilder< EntityType > {

	public static final List< FilteringOperation > SUPPORTED_OPERATORS = Arrays.asList( 
			FilteringOperation.EQUAL,
			FilteringOperation.NOT_EQUAL, 
			FilteringOperation.CONTAIN );

	@Override
	public Specification< EntityType > buildSpecification(SearchCriteria searchCriteria) {
		return new StringComparisonSpecification<>(searchCriteria);
	}

}
