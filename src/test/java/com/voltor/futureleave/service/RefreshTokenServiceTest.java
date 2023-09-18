package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.builder.AuthDataBuilder;
import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.RefreshTokenBuilder;
import com.voltor.futureleave.dao.RefreshTokenDao;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.service.exception.RefreshTokenNotFoundException;

public class RefreshTokenServiceTest extends BaseDaoServiceTest< RefreshToken, RefreshTokenService, RefreshTokenDao > {

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	@Mock
	private RefreshTokenDao refreshTokenDao;
	
	@Mock
	private AuthDataService authDataService;

	@Override
	public void additionalSetup() {

	}

	@Override
	public RefreshTokenService getService() {
		return refreshTokenService;
	}

	@Override
	public RefreshTokenDao getDao() {
		return refreshTokenDao;
	}

	@Override
	public RefreshToken createEntity() {
		return RefreshTokenBuilder.start().setSessionId( userAuthorizationService.getSessionId() ).build();
	}

	@Test
	public void setCurrentSessionTest() {
		given( getDao().create( any() ) ).willAnswer( i -> i.getArguments()[0] );
		RefreshToken refreshToken = getService().create( createEntity() );
		assertEquals( userAuthorizationService.getSessionId(), refreshToken.getSessionId() );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void returnAuthDatAndDeleteTest() {
		AuthData authData = AuthDataBuilder.start().build();
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).authData( authData ).mock( userAuthorizationService );
		when( authDataService.findSessionId( userAuthorizationService.getSessionId() ) )
			.thenReturn( authData );
		
		RefreshToken refreshToken = createEntity();
		
		when( getDao().getOne( any( Specification.class ) ) ).thenReturn( refreshToken );
		when( getDao().getOne( refreshToken.getId() ) ).thenReturn( refreshToken );
		
		AuthData tokenData = getService().getTokenData( "someToken" );
		assertEquals( refreshToken.getSessionId(), tokenData.getSessionId() );
		verify( getDao() ).delete( eq( refreshToken), anyBoolean() );
	}

	@Test
	public void shouldThrowException() {
		Exception exception = assertThrows( RefreshTokenNotFoundException.class,
				() -> getService().getTokenData( "UnexistingToken" ) );
		assertNotNull( exception );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldDeleteByToken() {
		RefreshToken refreshToken = createEntity();
		when( getDao().getOne( any( Specification.class ) ) ).thenReturn( refreshToken );
		getService().delete( refreshToken.getToken() );
		verify( getDao(), times( 1 ) ).delete( refreshToken, true );
	}
}
