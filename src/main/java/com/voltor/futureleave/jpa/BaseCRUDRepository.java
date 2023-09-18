package com.voltor.futureleave.jpa;

import org.springframework.data.repository.NoRepositoryBean;

import com.voltor.futureleave.model.Identifiable;

@NoRepositoryBean
public interface BaseCRUDRepository<EntityType extends Identifiable> extends PrimaryRepository<Long, EntityType> {
}
