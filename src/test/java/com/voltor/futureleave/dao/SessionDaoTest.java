package com.voltor.futureleave.dao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Collections;

import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.SessionBuilder;
import com.voltor.futureleave.dao.shared.AbstractIdentifiableDaoTest;
import com.voltor.futureleave.filtering.session.ArchivedSpecification;
import com.voltor.futureleave.jpa.SessionRepository;
import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.model.Role;

public class SessionDaoTest extends AbstractIdentifiableDaoTest< Session, SessionDao, SessionRepository > {

	@Mock
	private SessionRepository sessionRepository;

	@Override
	public Session createEntity() {
		return SessionBuilder.start().build();
	}

	@Override
	public SessionDao createDao() {
		return new SessionDao( userAuthorizationService, sessionRepository );
	}

	@Override
	public SessionRepository createRepository() {
		return sessionRepository;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void shouldReturnPageableRecordsIncludeArchived() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).mock( userAuthorizationService );
		given( createRepository().findAll( any( Specification.class ), any( Pageable.class ) ) )
				.willReturn( new PageImpl<>( Collections.singletonList( entity ) ) );
		ArchivedSpecification< Session > archivedSpecification = mock( ArchivedSpecification.class );

		PageRequest request = PageRequest.of( 0, 1, Sort.unsorted() );
		daoService.get( request, true );

		verify( createRepository() ).findAll( any( Specification.class ), any( Pageable.class ) );
		verifyNoInteractions( archivedSpecification );
	}

	@Override
	public void shouldReturnAllSortedRecordsIncludeArchived() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).mock( userAuthorizationService );
		super.shouldReturnAllSortedRecordsIncludeArchived();
	}

	@Override
	public void shouldReturnAllFilteredSortedRecordsWithIncludeArchived() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).mock( userAuthorizationService );
		super.shouldReturnAllFilteredSortedRecordsWithIncludeArchived();
	}

	@Override
	public void shouldReturnPageableFilteredRecordsWithIncludeArchived() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).mock( userAuthorizationService );
		super.shouldReturnPageableFilteredRecordsWithIncludeArchived();
	}
}
