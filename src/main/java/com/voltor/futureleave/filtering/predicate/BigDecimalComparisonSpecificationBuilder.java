package com.voltor.futureleave.filtering.predicate;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public class BigDecimalComparisonSpecificationBuilder implements SpecificationBuilder<Object> {

	public static final List<FilteringOperation> SUPPORTED_OPERATORS = Arrays.asList(
	        FilteringOperation.GREATER_THEN, FilteringOperation.LESS_THEN, FilteringOperation.GREATER_OR_EQUAL,
	        FilteringOperation.LESS_OR_EQUAL, FilteringOperation.EQUAL, FilteringOperation.NOT_EQUAL
	    );

	@Override
	public Specification<Object> buildSpecification(SearchCriteria searchCriteria) {
		return new BigDecimalComparisonSpecification<Object>(searchCriteria);
	}

}
