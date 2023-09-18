package com.voltor.futureleave.filtering.predicate;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;

public interface SpecificationBuilder< EntityType > {

	Specification< EntityType > buildSpecification( SearchCriteria searchCriteria );

}
