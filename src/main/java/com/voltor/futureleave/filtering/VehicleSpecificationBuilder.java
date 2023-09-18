package com.voltor.futureleave.filtering;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;
import com.voltor.futureleave.filtering.predicate.EqualingOrNullSpecificationBuilder;
import com.voltor.futureleave.filtering.predicate.ZonedDateTimeComparisonSpecificationBuilder;
import com.voltor.futureleave.model.Vehicle;

@Component
public class VehicleSpecificationBuilder implements EntityFilterSpecificationsBuilder< Vehicle > {

	private static final List< FilterableProperty< Vehicle > > FILTERABLE_PROPERTIES = List.of(
			new FilterableProperty<>( "brandName", String.class, EqualingOrNullSpecificationBuilder.SUPPORTED_OPERATORS, new EqualingOrNullSpecificationBuilder<>() ),
			new FilterableProperty<>( "brandType", String.class, EqualingOrNullSpecificationBuilder.SUPPORTED_OPERATORS, new EqualingOrNullSpecificationBuilder<>() ),
			new FilterableProperty<>( "licensePlate", String.class, EqualingOrNullSpecificationBuilder.SUPPORTED_OPERATORS, new EqualingOrNullSpecificationBuilder<>() ),
			new FilterableProperty<>( "startDate", ZonedDateTime.class, ZonedDateTimeComparisonSpecificationBuilder.SUPPORTED_OPERATORS, new ZonedDateTimeComparisonSpecificationBuilder<>() ),
			new FilterableProperty<>( "endDate", ZonedDateTime.class, ZonedDateTimeComparisonSpecificationBuilder.SUPPORTED_OPERATORS, new ZonedDateTimeComparisonSpecificationBuilder<>() ));

	@Override
	public List< FilterableProperty< Vehicle > > getFilterableProperties() {
		return FILTERABLE_PROPERTIES;
	}
}