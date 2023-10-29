package com.voltor.futureleave.filtering.predicate;

import java.util.List;

import com.voltor.futureleave.filtering.FilterableProperty;
import com.voltor.futureleave.model.Session;

public class SessionSpecificationBuilder implements  EntityFilterSpecificationsBuilder < Session> {
	
	private static final List< FilterableProperty< Session > > FILTERABLE_PROPERTIES = List.of(
			new FilterableProperty<>( "name", String.class, EqualingOrNullSpecificationBuilder.SUPPORTED_OPERATORS, new EqualingOrNullSpecificationBuilder<>() ) );

	@Override
	public List< FilterableProperty< Session > > getFilterableProperties() {
		return FILTERABLE_PROPERTIES;
	}
}
