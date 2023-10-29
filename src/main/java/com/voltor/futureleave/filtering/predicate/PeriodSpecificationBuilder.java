package com.voltor.futureleave.filtering.predicate;

import java.util.List;

import org.springframework.stereotype.Component;

import com.voltor.futureleave.filtering.FilterableProperty;
import com.voltor.futureleave.model.Period;

@Component
public class PeriodSpecificationBuilder implements EntityFilterSpecificationsBuilder<Period> {

	private static final List<FilterableProperty<Period>> FILTERABLE_PROPERTIES = List
			.of(new FilterableProperty<>("sessionId", Long.class, EqualingOrNullSpecificationBuilder.SUPPORTED_OPERATORS,
					new EqualingOrNullSpecificationBuilder<>()));

	@Override
	public List<FilterableProperty<Period>> getFilterableProperties() {
		return FILTERABLE_PROPERTIES;
	}
}
