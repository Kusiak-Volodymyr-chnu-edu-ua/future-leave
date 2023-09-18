package com.voltor.futureleave.filtering.predicate;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Identifiable;

public class InIdsSpecificationBuilder< EntityType extends Identifiable >  implements SpecificationBuilder< EntityType >  {

	public static final List<FilteringOperation> SUPPORTED_OPERATORS = Arrays.asList(FilteringOperation.IN, FilteringOperation.NOT_IN);

	@Override
	public Specification< EntityType > buildSpecification(SearchCriteria searchCriteria) {
		return new InIdsSpecification<>(searchCriteria);
	}

}
