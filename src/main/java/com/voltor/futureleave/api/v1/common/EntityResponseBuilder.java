package com.voltor.futureleave.api.v1.common;

import java.util.List;

import com.voltor.futureleave.model.Identifiable;

public interface EntityResponseBuilder<T extends Identifiable, R extends AbstractResponse> {

	R convertEntityToResponse(T entity);

	R convertEntityToResponse(T entity, List<String> entitiesToExpand);
}
