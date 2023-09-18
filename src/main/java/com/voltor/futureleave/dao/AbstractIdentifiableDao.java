package com.voltor.futureleave.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.core.GenericTypeResolver;

import com.voltor.futureleave.dao.specification.InSpecification;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.predicate.EqualingSpecification;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.service.AuthenticatedUserService;

public abstract class AbstractIdentifiableDao<T extends Identifiable> extends AbstractDao<Long, T> {
	public AbstractIdentifiableDao(AuthenticatedUserService userAuthorizationService) {
		super(userAuthorizationService);
	}
	
	public List< T > getByIds( Collection< Long > ids ) {
		if ( ids == null || ids.isEmpty() ) {
			return Collections.emptyList();
		}

		InSpecification< T, Long > inSpecification = new InSpecification<>( "id", ids );
		return getRepository().findAll( addSpecifications().and( inSpecification ) );

	}
	
	public T getById( Long id ) {
		EqualingSpecification< T > specification = new EqualingSpecification<>( new SearchCriteria( "id", FilteringOperation.EQUAL, id ) );
		return getOne( specification );

	}
	
	@Override
	public boolean isEditAllowed(T item) {
		return super.isEditAllowed( item ) || getById( item.getId() ) != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Class<T>[] getEntityTypeClasses() {
		return (Class<T>[]) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractIdentifiableDao.class);
	}
}
