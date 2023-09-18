package com.voltor.futureleave.dao.shared;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.predicate.EqualingSpecification;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.filtering.session.ArchivedSpecification;
import com.voltor.futureleave.jpa.BaseCRUDRepository;
import com.voltor.futureleave.model.Identifiable;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.service.AuthenticatedUserService;

@SuppressWarnings("unchecked")
public abstract class AbstractIdentifiableDaoTest<EntityType extends Identifiable, 
	DaoType extends AbstractIdentifiableDao<EntityType>, 
	RepositoryType extends BaseCRUDRepository<EntityType>> {
	
	@Mock
	protected AuthenticatedUserService userAuthorizationService;
	protected DaoType daoService;
	protected EntityType entity;

	public abstract EntityType createEntity();

	public abstract DaoType createDao();

	public abstract RepositoryType createRepository();

	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks(this).close();
		daoService = createDao();
		entity = createEntity();
		entity.setId( 1L );
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
	}

	@Test
	public void testRootAbleToEdit() {
		assertTrue( daoService.isEditAllowed( entity ) );
	}

	@Test
	public void shouldReturnPageableRecordsIncludeArchived() {
		given( createRepository().findAll( any( Specification.class ), any( Pageable.class ) ) )
				.willReturn( new PageImpl<>( Collections.singletonList( entity ) ) );
		ArchivedSpecification< EntityType > archivedSpecification = mock( ArchivedSpecification.class );

		PageRequest request = PageRequest.of( 0, 1, Sort.unsorted() );
		daoService.get( request, true );

		verify( createRepository() ).findAll( any( Specification.class ), any( Pageable.class ) );
		verifyNoInteractions( archivedSpecification );
	}

	@Test
	public void shouldReturnAllSortedRecordsIncludeArchived() {
		given( createRepository().findAll( any( Specification.class ), any( Sort.class ) ) )
				.willReturn( Collections.singletonList( entity ) );
		ArchivedSpecification< EntityType > archivedSpecification = mock( ArchivedSpecification.class );

		daoService.get( Sort.unsorted(), true );

		verify( createRepository() ).findAll( any( Specification.class ), any( Sort.class ) );
		verifyNoInteractions(archivedSpecification);
	}

	@Test
	public void shouldReturnAllFilteredSortedRecordsWithIncludeArchived() {
		given( createRepository().findAll( any( Specification.class ), any( Sort.class ) ) )
				.willReturn( Collections.singletonList( entity ) );
		ArchivedSpecification< EntityType > archivedSpecification = mock( ArchivedSpecification.class );

		Specification< EntityType > specification = new EqualingSpecification<>(
				new SearchCriteria( "name", FilteringOperation.EQUAL, "entity" ) );
		daoService.get( specification, Sort.unsorted(), true );

		verify( createRepository() ).findAll( any( Specification.class ), any( Sort.class ) );
		verifyNoInteractions( archivedSpecification );
	}

	@Test
	public void shouldReturnPageableFilteredRecordsWithIncludeArchived() {
		given( createRepository().findAll( any( Specification.class ), any( Pageable.class ) ) )
				.willReturn( new PageImpl<>( Collections.singletonList( entity ) ) );
		ArchivedSpecification< EntityType > archivedSpecification = mock( ArchivedSpecification.class );

		Specification< EntityType > specification = new EqualingSpecification<>(
				new SearchCriteria( "name", FilteringOperation.EQUAL, "entity" ) );
		PageRequest request = PageRequest.of( 0, 1, Sort.unsorted() );
		daoService.get( specification, request, true );

		verify( createRepository() ).findAll( any( Specification.class ), any( Pageable.class ) );
		verifyNoInteractions( archivedSpecification );
	}
}
