package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.SessionBuilder;
import com.voltor.futureleave.dao.SessionDao;
import com.voltor.futureleave.dao.exception.ActionNotAllowedException;
import com.voltor.futureleave.filtering.session.IdSpecification;
import com.voltor.futureleave.jpa.SessionRepository;
import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.model.Role;

public class SessionServiceTest {

	@Mock
	protected AuthenticatedUserService userAuthorizationService;

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private SessionDao sessionDao;

	@InjectMocks
	private SessionService sessionService;

	private Session session1;
	private Session session2;

	private Specification<Session> session_user_specifications;

	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks(this).close();
		session1 = SessionBuilder.start().build();
		session2 = SessionBuilder.start().build();

		session_user_specifications = Specification.where( null );
		IdSpecification< Long, Session > idSpecification = new IdSpecification<>( session1.getId() );
		session_user_specifications = session_user_specifications.and( idSpecification );
	}

	@Test
	public void shouldNotAllowEditingIfCurrentUserIsNotRoot() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		assertFalse( sessionService.editingIsAllowed( session2 ) );
	}

	@Test
	public void shouldFindAllWithSpecificationOnGetAll() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		sessionService.get();
		verify( sessionDao ).get();
	}

	@Test
	public void shouldFindAllWithSpecificationAndPageableOnGetAllWithPageable() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		PageRequest request = PageRequest.of( 1, 20, Sort.by( Direction.ASC, "id" ) );
		sessionService.get( request );
		verify( sessionDao ).get( request, false );
	}

	@Test
	public void shouldFindAllWithSpecificationAndSortOnGetAllWithSort() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		Sort sort = Sort.by( Direction.ASC, "id" );
		sessionService.get( sort );
		verify( sessionDao ).get( eq( sort ), anyBoolean() );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldFindAllWithSpecificationOnGetRecords() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		sessionService.get( Specification.where( null ) );
		verify( sessionDao ).get( any( Specification.class ) );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldFindAllWithSpecificationAndPageableOnGetRecordsWithPageable() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		PageRequest request = PageRequest.of( 1, 20, Sort.by( Direction.ASC, "id" ) );
		sessionService.get( Specification.where( null ), request );
		verify( sessionDao ).get( any( Specification.class ), eq( request ), anyBoolean() );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldFindAllWithSpecificationAndSortOnGetRecordsWithSort() {
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
		Sort sort = Sort.by( Direction.ASC, "id" );
		sessionService.get( Specification.where( null ), sort );
		verify( sessionDao ).get( any( Specification.class ), eq( sort ), anyBoolean() );
	}

	@Test
	public void shouldNotAllowDelete() {
		assertThrows( ActionNotAllowedException.class, () -> sessionService.delete( 1L, true ) );
	}
}