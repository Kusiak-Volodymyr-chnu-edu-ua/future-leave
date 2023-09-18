package com.voltor.futureleave.api.v1.common;

import com.voltor.futureleave.filtering.predicate.EntityFilterSpecificationsBuilder;

public interface FilterableController<T> {

	default EntityFilterSpecificationsBuilder<T> getFilterSpecificationsBuilder() {
		return null;
	}
}
