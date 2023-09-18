package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.dao.AuthDataDao;
import com.voltor.futureleave.dao.specification.EqualSpecification;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.service.exception.ActionNotSupportedException;

class AuthDataServiceTest {

	@Mock private AuthDataDao authDataDao;
	@InjectMocks private AuthDataService authDataService;
	
	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks( this ).close();
	}

	@Test
	void createNotSupported() {
		AuthData entity = mock( AuthData.class );
		
		assertThrows( ActionNotSupportedException.class, () -> authDataService.create( entity ) );
		
		verify( authDataDao, times( 0 ) ).create( any( AuthData.class ) );
	}

	@SuppressWarnings("unchecked")
	@Test
	void updateNotSupported() {
		AuthData entity = mock( AuthData.class );
		assertThrows( ActionNotSupportedException.class, () -> authDataService.update( entity ) );
		assertThrows( ActionNotSupportedException.class, () -> authDataService.update( entity, true ) );
		
		verify( authDataDao, times( 0 ) ).update( any( AuthData.class ) );
		verify( authDataDao, times( 0 ) ).update( any( Iterable.class ) );
	}

	@Test
	void deleteNotSupported() {
		Specification< AuthData > specification = new EqualSpecification<>( "id", 1L );
		
		assertThrows( ActionNotSupportedException.class, () -> authDataService.delete( 1L, true ) );
		assertThrows( ActionNotSupportedException.class, () -> authDataService.delete( specification, true ) );
		
		verify( authDataDao, times( 0 ) ).delete( any( AuthData.class ) );
		verify( authDataDao, times( 0 ) ).delete( any( AuthData.class ), anyBoolean() );
	}
	
	@Test
	void findByCliendId() {
		String value = "any text";
		authDataService.findByClientId( value );
		
		Specification< AuthData > specification = new EqualSpecification<>( "clientId", value ); 
		verify( authDataDao, times( 1 ) ).getOne( specification );
	}
	
	@Test
	void findBySessionId() {
		Long value = Long.valueOf("08098");
		authDataService.findSessionId( value );
		
		Specification< AuthData > specification = new EqualSpecification<>( "sessionId", value ); 
		verify( authDataDao, times( 1 ) ).getOne( specification );
	}

}
