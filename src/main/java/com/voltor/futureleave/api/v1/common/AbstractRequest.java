package com.voltor.futureleave.api.v1.common;

public abstract class AbstractRequest<EntityType> {

	public abstract EntityType createEntity();

	public abstract EntityType updateEntity(EntityType entity);
}
