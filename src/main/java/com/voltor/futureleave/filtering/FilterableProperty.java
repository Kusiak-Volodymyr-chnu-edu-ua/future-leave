package com.voltor.futureleave.filtering;

import java.util.List;

import com.voltor.futureleave.filtering.predicate.SpecificationBuilder;

/**
 * Define a entity property which support filtering
 */
public class FilterableProperty< EntityType > {
	/**
	 * entity object property name
	 */
	private final String propertyName;
	/**
	 * property type
	 */
	private final Class< ? > expectedType;
	/**
	 * allowed operations for this property
	 */
	private final List< FilteringOperation > operators;
	/**
	 * helper class used to build {@link org.springframework.data.jpa.domain.Specification} object for specified property
	 */
	private final SpecificationBuilder< EntityType> specificationBuilder;

	public FilterableProperty( String propertyName, 
			Class< ? > expectedType, List< FilteringOperation > operators,
			SpecificationBuilder< EntityType > specificationBuilder ) {
		this.propertyName = propertyName;
		this.expectedType = expectedType;
		this.operators = operators;
		this.specificationBuilder = specificationBuilder;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class< ? > getExpectedType() {
		return expectedType;
	}

	public List< FilteringOperation > getOperators() {
		return operators;
	}

	public SpecificationBuilder< EntityType > getSpecificationBuilder() {
		return specificationBuilder;
	}
}
