package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.model.Role;

/** Base class for most recent Service (DAO) tests */
public abstract class BaseDaoServiceTest<
	T extends Identifiable,
	Service extends AbstractService< T >, 
	Dao extends AbstractIdentifiableDao< T > > {
	
	@Mock
	AuthenticatedUserService userAuthorizationService;
	
	protected T entity;
	protected Class<T> entityClass;

	abstract void additionalSetup();

	abstract Service getService();

	abstract Dao getDao();

	abstract T createEntity();

	@SuppressWarnings("unchecked")
	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.openMocks( this ).close();
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		additionalSetup();
		entity = createEntity();
		entityClass = (Class< T >) entity.getClass();
	}

	@Test
	void shouldCreateEntity() {
		given( getDao().create( any( entityClass ) ) ).willReturn( entity );
		ArgumentCaptor< T > argumentCaptor = ArgumentCaptor.forClass( entityClass );

		getService().create( entity );

		verify( getDao() ).create( argumentCaptor.capture() );
		assertEquals( entity, argumentCaptor.getValue() );
	}

	@Test
	void shouldUpdateEntity() {
		given( getDao().update( any( entityClass ) ) ).willReturn( entity );
		ArgumentCaptor< T > argumentCaptor = ArgumentCaptor.forClass( entityClass );

		getService().update( entity );

		verify( getDao() ).update( argumentCaptor.capture(), anyBoolean() );
		assertEquals( entity, argumentCaptor.getValue() );
	}

	@Test
	void shouldDeleteEntity() {
		given( getDao().getOne( entity.getId() ) ).willReturn( entity );
		ArgumentCaptor< T > argumentCaptor = ArgumentCaptor.forClass( entityClass );

		getService().delete( entity.getId() );

		verify( getDao() ).delete( argumentCaptor.capture(), anyBoolean() );
		assertEquals( entity, argumentCaptor.getValue() );
	}
}
