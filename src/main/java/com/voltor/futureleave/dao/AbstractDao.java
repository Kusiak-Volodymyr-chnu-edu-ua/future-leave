package com.voltor.futureleave.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;

import com.voltor.futureleave.dao.exception.LocalizationUtils;
import com.voltor.futureleave.dao.exception.MissingPropertyException;
import com.voltor.futureleave.dao.specification.EqualSpecification;
import com.voltor.futureleave.dao.specification.InCollectionSpecification;
import com.voltor.futureleave.dao.specification.SpecificationUtil;
import com.voltor.futureleave.dao.specification.SubclassEqualSpecification;
import com.voltor.futureleave.filtering.predicate.EqualStringSpecification;
import com.voltor.futureleave.filtering.session.ArchivedSpecification;
import com.voltor.futureleave.filtering.session.IdSpecification;
import com.voltor.futureleave.filtering.user.UserSpecification;
import com.voltor.futureleave.jpa.PrimaryRepository;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;
import com.voltor.futureleave.model.Archivable;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.model.PrimaryEntity;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.model.UserTenencyEntity;
import com.voltor.futureleave.service.AuthenticatedUserService;
import com.voltor.futureleave.service.exception.RetrievalNotAllowedException;

public abstract class AbstractDao<IdType, T extends PrimaryEntity<IdType>> {

	protected final AuthenticatedUserService userAuthorizationService;

	public AbstractDao(AuthenticatedUserService userAuthorizationService) {
		this.userAuthorizationService = userAuthorizationService;
	}

	protected abstract PrimaryRepository<IdType, T> getRepository();

	protected Specification<T> addAdditionalSpecificationsForGet() {
		return null;
	}
	/**
	 * Return whether editing of the EntityType is allowed for the current user.Under editing falls Updating and Removing.
	 *
	 * @param entity
	 * @return
	 */
	public boolean isEditAllowed(T entity) {
		return userAuthorizationService.isRoot();
	}

	/**
	 * Checks if there are records matching given specification. Also specification from {@link #addSpecifications() }
	 * are taken into account.
	 *
	 * @param filter The filter to match
	 * @return
	 */
	public boolean exists(Specification<T> filter) {
		return this.existsIncludingArchived(filter, false);
	}

	public boolean existsIncludingArchived(Specification<T> filter, boolean includeArchivedEntities) {
		final Specification<T> specification = addSpecifications(includeArchivedEntities).and(filter);
		return getRepository().count(specification) > 0;
	}

	/**
	 * Retrieve all records for the specific EntityType, using a pageable.
	 * If the EntityType is a Archived EntityType, only non archived records will be returned.
	 * If the currently logged in user has the ROOT role, all entities (non archived) entities will be returned.
	 * Otherwise, only the (non archived) entities that belong to the currently logged in user's session will be returned.
	 *
	 * @param pageable the Pageable that specifies page size and page to retrieve.
	 * @return the records found or a empty list (if no records are found that comply to the above criteria).
	 */
	public Page<T> get(Pageable pageable) {
		return get(pageable, false);
	}

	/**
	 * Retrieve all records for the specific EntityType, using a pageable.
	 * If the EntityType is a Archived EntityType and includeArchived equals true, records include archived will be returned.
	 * If the currently logged in user has the ROOT role, all entities (non archived) entities will be returned.
	 * Otherwise, only the (non archived) entities that belong to the currently logged in user's session will be returned.
	 *
	 * @param pageable        the Pageable that specifies page size and page to retrieve.
	 * @param includeArchived the boolean that specifies if is needed to include archived records
	 * @return the records found or a empty list (if no records are found that comply to the above criteria).
	 */
	public Page<T> get(Pageable pageable, boolean includeArchived) {

		Specification<T> specification = addSpecifications(includeArchived).and(addAdditionalSpecificationsForGet());

		Sort sort = pageable.getSort();
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<T> page = getRepository().findAll(specification, pageRequest);

		hasReadPermission(page.getContent());

		return page;
	}

	/**
	 * Retrieve all records for the specific EntityType.
	 * If the EntityType is a Archived EntityType, only non archived records will be returned.
	 * If the currently logged in user has the ROOT role, all entities (non archived) entities will be returned.
	 * Otherwise, only the (non archived) entities that belong to the currently logged in user's session will be returned.
	 *
	 * @return the records found or a empty list (if no records are found that comply to the above criteria).
	 */
	public List<T> get() {

		Specification<T> specification = addSpecifications().and(addAdditionalSpecificationsForGet());
		List<T> list = getRepository().findAll(specification);

		hasReadPermission(list);

		return list;
	}

	public List<T> get(Sort sort) {
		return get(sort, false);
	}

	public List<T> get(Sort sort, boolean includeArchived) {
		Specification<T> specification = addSpecifications(includeArchived).and(addAdditionalSpecificationsForGet());

		try {
			List<T> list = getRepository().findAll(specification, sort);

			hasReadPermission(list);

			return list;
		} catch (PropertyReferenceException ex) {
			final LocalizedMessage message = new LocalizedMessage(ex.getMessage(), LocalizedExceptionCode.MISSING_PROPERTY_EXCEPTION_MESSAGE);
			throw new MissingPropertyException(message);
		}
	}

	public Page<T> get(Specification<T> filter, Pageable pageable) {
		return get(filter, pageable, false);
	}

	public Page<T> get(Specification<T> filter, Pageable pageable, boolean includeArchived) {
		Specification<T> specification = addSpecifications(includeArchived).and(filter).and(addAdditionalSpecificationsForGet());

		Page<T> page = getRepository().findAll(specification, pageable);

		hasReadPermission(page.getContent());

		return page;
	}

	public List<T> getIncludeArchived(Specification<T> filter) {
		Specification<T> specification = addSpecifications(true).and(filter);
		return getRepository().findAll(specification);
	}

	public List<T> get(Specification<T> filter) {
		return this.get(filter, false);
	}

	public List<T> get(Specification<T> filter, boolean ignorePermissions) {
		Specification<T> specification = addSpecifications().and(filter).and(addAdditionalSpecificationsForGet());

		List<T> list = getRepository().findAll(specification);

		if (!ignorePermissions) {
			hasReadPermission(list);
		}

		return list;
	}

	public List<T> get(Specification<T> filter, Sort sort) {
		return get(filter, sort, false);
	}

	public List<T> get(Specification<T> filter, Sort sort, boolean includeArchived) {
		Specification<T> specification = addSpecifications(includeArchived).and(filter).and(addAdditionalSpecificationsForGet());

		try {
			List<T> list = getRepository().findAll(specification, sort);

			hasReadPermission(list);

			return list;
		} catch (PropertyReferenceException ex) {
			final LocalizedMessage message = new LocalizedMessage(ex.getMessage(), LocalizedExceptionCode.MISSING_PROPERTY_EXCEPTION_MESSAGE);
			throw new MissingPropertyException(message);
		}
	}


	protected List<T> getByField(String field, Object value) {
		return this.getByField(field, value, false);
	}

	protected List<T> getByField(String field, Object value, boolean ignorePermissions) {
		EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
		return get(equalSpecification, ignorePermissions);
	}

	protected List<T> getByField(String field, Object value, Sort sort) {
		EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
		return get(equalSpecification, sort);
	}

	protected List<T> getByFieldIncludeArchived(String field, Object value) {
		EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
		return getIncludeArchived(equalSpecification);
	}

	protected boolean existsByField(String field, Object value) {
		final EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
		return exists(equalSpecification);
	}

	protected <R extends Identifiable> List<T> getByFieldInCollection(String field, R value) {
		return this.getByFieldInCollection(field, value, false);
	}

	protected <R extends Identifiable> List<T> getByFieldInCollection(String field, R value, boolean ignorePermissions) {
		InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(getEntityTypeClasses()[0], field, value);
		return get(inCollectionSpecification, ignorePermissions);
	}

	protected <R extends Identifiable> List<T> getByFieldInCollectionIncludeArchived(String field, R value) {
		InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(getEntityTypeClasses()[0], field, value);
		return getIncludeArchived(inCollectionSpecification);
	}

	protected <R extends Identifiable> T getOneByFieldInCollection(String field, R value) {
		InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(getEntityTypeClasses()[0], field, value);
		return getOne(inCollectionSpecification, false);
	}

	protected <R extends Identifiable> boolean existsByFieldInCollection(String field, R value) {
		final InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(
				getEntityTypeClasses()[0], field, value);
		return exists(inCollectionSpecification);
	}

	public T getOne(Specification<T> filter, boolean ignorePermissions) {
		Specification<T> specification = addSpecifications().and(filter);
		T entity = getRepository().findOne(specification).orElse(null);

		if (!ignorePermissions) {
			hasReadPermission(Collections.singletonList(entity));
		}

		return entity;
	}

	protected <S extends T> List<T> getBySubclassField(String field, Object value, Class<S> subclassClass) {
		return this.getBySubclassField(field, value, subclassClass, false);
	}

	protected <S extends T> List<T> getBySubclassField(String field, Object value, Class<S> subclassClass, boolean ignorePermissions) {
		SubclassEqualSpecification<T, S> subclassEqualSpecification = new SubclassEqualSpecification<>(field, value, subclassClass);
		return get(subclassEqualSpecification, ignorePermissions);
	}

	/**
	 * Retrieve a record for the specific EntityType based on its ID.
	 * If the EntityType is a Archived EntityType, only a non archived record will be returned.
	 * If the currently logged in user has the ROOT role, a (non archived) entity will be returned regardless of its session.
	 * Otherwise, the (non archived) entity will only be returned if it belongs to the currently logged in user's session.
	 *
	 * @param id the ID of the record to be found.
	 * @return the record found or null (if no record is found that complies to the above criteria).
	 */
	public T getOne(IdType id) {
		return getOneArchived(id, false);
	}

	public T getOne(Specification<T> specification) {
		return this.getOneArchived(specification, false);
	}

	public T getOneArchived(IdType id, boolean includeArchivedEntities) {
		return getOneArchived(id, includeArchivedEntities, null);
	}

	public T getOneArchived(IdType id, boolean includeArchivedEntities, Specification<T> additionalSpecification) {
		if (id == null) return null;
		Specification<T> mainSpecification = addSpecifications(includeArchivedEntities).and(new IdSpecification<>(id));

		if (additionalSpecification != null) {
			mainSpecification = additionalSpecification.and(mainSpecification);
		}

		return getRepository().findOne(mainSpecification).orElse(null);
	}
	
	public T getFirst(Specification<T> specification, boolean includeArchivedEntities) {
		Specification<T> specifications = addSpecifications(includeArchivedEntities).and(specification);

		List< T > result = getRepository().findAll(specifications);
		return result.isEmpty() ? null : result.get( 0 );
	}


	public T getOneArchived(Specification<T> specification, boolean includeArchivedEntities) {
		Specification<T> specifications = addSpecifications(includeArchivedEntities).and(specification);

		return getRepository().findOne(specifications).orElse(null);
	}

	protected List<T> getByStringFieldIgnoreCase(String field, String value, boolean ignorePermissions) {
		EqualStringSpecification<T> equalSpecification = new EqualStringSpecification<>(field, value, true);
		return get(equalSpecification, ignorePermissions);
	}

	public long getCount(Specification<T> filter) {
		PageRequest pageable = PageRequest.of(0, 1, Sort.unsorted());
		Page<T> records = get(filter, pageable);
		return records.getTotalElements();
	}

	/**
	 * Create a new entity with the contents of entity.
	 *
	 * @param entity the record to be created.
	 * @return The created entity.
	 */
	public T create(T entity) {
		return getRepository().save(entity);
	}

	public T update( T entity ) {
		return update( entity, false );
	}

	public Iterable< T > update( Iterable< T > entities ) {
		for (T entity : entities) {
			if ( !isEditAllowed( entity ) ) {
				throw LocalizationUtils.buildUpdateNotAllowedExceptionNoPermission( entity );
			}
		}
		return getRepository().saveAll(entities);
	}

	/**
	 * Update a entity with the contents of entity based on its ID.
	 * A entity must be found using the @link #getOne(long) logic in order for it to be updated.
	 *
	 * @param entity            the entity to update.
	 * @param ignorePermissions enables/disables the editingIsAllowed check
	 * @return The updated entity.
	 */
	public T update(T entity, boolean ignorePermissions) {

		if (!ignorePermissions && !isEditAllowed(entity)) {
			throw LocalizationUtils.buildUpdateNotAllowedExceptionNoPermission(entity);
		}

		return getRepository().save(entity);
	}

	/**
	 * Delete a entity for the specific EntityType based on its ID.The entity will only be deleted if it can be found using the @link #getOne(long) logic.
	 * If the entity is of the {@link Archivable} EntityType, it will not be deleted but archived instead.
	 *
	 * @param entity
	 */
	public void delete(T entity) {
		delete(entity, false);
	}

	/**
	 * Delete a entity for the specific EntityType based on its ID.The entity will only be deleted if it can be found using the @link #getOne(long) logic.
	 * If the entity is of the Archived EntityType, it will not be deleted but archived instead.
	 *
	 * @param entity
	 * @param ignorePermissions enables/disables the editingIsAllowed check
	 */
	public void delete(T entity, boolean ignorePermissions) {
		if (!ignorePermissions && !isEditAllowed(entity)) {
			throw LocalizationUtils.buildRemovalNotAllowedExceptionNoPermission(entity);
		}

		if (entityIsArchived()) {
			((Archivable) entity).setArchived(true);
			getRepository().save(entity);
		} else {
			getRepository().deleteById(entity.getId());
		}
	}

	protected Specification<T> addSpecifications() {
		return addSpecifications(false);
	}

	protected Specification< T > addSpecifications( boolean includeArchivedEntities ) {
		Specification< T > specification = SpecificationUtil.initEmptySpec();

		if ( entityIsUser() ) {
			return specification;
		}
		
		if ( entityIsExtendedByUser() && !userAuthorizationService.isRoot() ) {
			Long currentUserId = userAuthorizationService.getCurrentUserId();
			UserSpecification< T > sessionSpecification = new UserSpecification<>( currentUserId );
			specification = specification.and( sessionSpecification );
		}

		if ( !includeArchivedEntities && entityIsArchived() ) {
			ArchivedSpecification< T > archivedSpecification = new ArchivedSpecification<>();
			specification = specification.and( archivedSpecification );
		}

		return specification;
	}

	/**
	 * If user has no permission to a record on the list then give exception.
	 *
	 * @param entities
	 * @throws {@link RetrievalNotAllowedException}
	 */
	public void hasReadPermission(List<T> entities) {
	}

	@SuppressWarnings("unchecked")
	protected Class<T>[] getEntityTypeClasses() {
		return (Class<T>[]) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractDao.class);
	}

	private boolean entityIsUser() {
		Class<T>[] entityTypeClasses = getEntityTypeClasses();
		return entityTypeClasses[0].equals(User.class);
	}

	private boolean entityIsArchived() {
		Class<T>[] entityTypeClasses = getEntityTypeClasses();
		return Archivable.class.isAssignableFrom(entityTypeClasses[0]);
	}

	private boolean entityIsExtendedByUser() {
		Class<T>[] entityTypeClasses = getEntityTypeClasses();
		return UserTenencyEntity.class.isAssignableFrom(entityTypeClasses[0]);
	}
}
