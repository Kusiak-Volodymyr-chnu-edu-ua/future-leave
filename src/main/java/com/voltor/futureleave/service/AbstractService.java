package com.voltor.futureleave.service;

import static java.lang.String.format;

import java.util.Collection;
import java.util.List;

import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.model.Identifiable;

public abstract class AbstractService<T extends Identifiable> {


	protected abstract AbstractIdentifiableDao<T> getDao();

	public Page<T> get(Pageable pageable) {
		return getDao().get(pageable, false);
	}

	public List<T> get() {
		return getDao().get();
	}

	public List<T> get(Sort sort) {
		return getDao().get(sort, false);
	}

	public Page<T> get(Specification<T> filter, Pageable pageable) {
		return getDao().get(filter, pageable, false);
	}

	public List<T> get(Specification<T> filter) {
		return getDao().get(filter);
	}

	public List<T> get(Specification<T> filter, Sort sort) {
		return getDao().get(filter, sort, false);
	}
	
	public List<T> getByIds(Collection<Long> ids) {
		return getDao().getByIds(ids);
	}

	public T getOne(Long id) {
		return getDao().getOne(id);
	}
	
	public T getOne(Specification<T> filter) {
		return getDao().getOne(filter);
	}

	@Transactional
	public T create(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException(format("Can not create Null %s entity", getEntityTypeClass().getSimpleName()));
		}

		beforeCreate(entity);
		entity = getDao().create(entity);
		afterCreate(entity);
		return entity;
	}

	/**
	 * Method to override when additional create logic is needed
	 *
	 * @param entity the entity to be created
	 */
	protected void beforeCreate(T entity) {
	}

	/**
	 * Method to override when additional create logic is needed
	 *
	 * @param entity the entity to be created
	 */
	protected void afterCreate(T entity) {
	}

	@Transactional
	public T update( T entity ) {
		return this.update( entity, false);
	}

	@Transactional
	public T update( T entity, boolean ignorePermissions ) {
		if ( entity == null ) {
			throw new IllegalArgumentException( String.format( "Can not update Null %s entity", getEntityTypeClass().getSimpleName() ) );
		}

		beforeUpdate( entity );
		entity = getDao().update( entity, ignorePermissions );
		afterUpdate( entity );
		return entity;
	}

	/**
	 * Method to override when additional update logic is needed
	 *
	 * @param entity the entity to be updated
	 */
	protected void beforeUpdate(T entity) {
	}

	/**
	 * Method to override when additional update logic is needed
	 *
	 * @param entity the entity to be updated
	 */
	protected void afterUpdate(T entity) {
	}

	@Transactional
	public void delete(long id) {
		this.delete(id, false);
	}

	@Transactional
	public void delete(long id, boolean ignorePermissions) {
		T entity = getDao().getOne(id);
		if (entity == null) {
			throw new IllegalArgumentException(format("Can not delete. Record with ID: %d for class %s not found. ", id, getEntityTypeClass().getSimpleName()));
		}
		delete(entity, ignorePermissions);
	}

	@Transactional
	protected void delete(Specification<T> specification, boolean ignorePermissions) {
		T entity = getDao().getOne(specification);
		if (entity == null) {
			throw new IllegalArgumentException(
					format("Can not delete. Record by specification for class %s not found. ", getEntityTypeClass().getSimpleName())
			);
		}
		delete(entity, ignorePermissions);
	}

	private void delete(T entity, boolean ignorePermissions) {
		beforeDelete(entity);
		getDao().delete(entity, ignorePermissions);
		afterDelete(entity);
	}

	/**
	 * Method to override when additional deletion logic is needed
	 *
	 * @param entity the entity to be deleted
	 */
	protected void beforeDelete(T entity) {
	}

	/**
	 * Method to override when additional delete logic is needed
	 *
	 * @param entity the entity to be deleted
	 */
	protected void afterDelete(T entity) {
	}

	private Class<T> getEntityTypeClass() {
		@SuppressWarnings("unchecked")
		Class<T>[] classes = (Class<T>[]) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractService.class);
		return classes[0];
	}

	public boolean editingIsAllowed(T entity) {
		return getDao().isEditAllowed(entity);
	}

}
