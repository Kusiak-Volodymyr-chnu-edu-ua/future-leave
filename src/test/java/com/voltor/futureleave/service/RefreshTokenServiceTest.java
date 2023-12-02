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

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.RefreshTokenBuilder;
import com.voltor.futureleave.builder.UserBuilder;
import com.voltor.futureleave.dao.RefreshTokenDao;
import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.exception.RefreshTokenNotFoundException;

public class RefreshTokenServiceTest extends BaseDaoServiceTest< RefreshToken, RefreshTokenService, RefreshTokenDao > {

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	@Mock
	private RefreshTokenDao refreshTokenDao;
	
	@Mock
	private UserService userService;

	@Override
	public void additionalSetup() {
		User currentUser = userAuthorizationService.getCurrentUser();
		when( userService.getOne( currentUser.getId() ) ).thenReturn( currentUser );
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
		return RefreshTokenBuilder.start().setUser( userAuthorizationService.getCurrentUser() ).build();
	}

	@Test
	public void setCurrentSessionTest() {
		given( getDao().create( any() ) ).willAnswer( i -> i.getArguments()[0] );
		RefreshToken refreshToken = getService().create( createEntity() );
		assertEquals( userAuthorizationService.getCurrentUser(), refreshToken.getUser() );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void returnAuthDatAndDeleteTest() {
		User user = UserBuilder.start().build();
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).user( user ).mock( userAuthorizationService );
		when( userService.findByLogin( userAuthorizationService.getCurrentUser().getLogin() ) )
			.thenReturn( user );
		
		RefreshToken refreshToken = createEntity();
		
		when( getDao().getOne( any( Specification.class ) ) ).thenReturn( refreshToken );
		when( getDao().getOne( refreshToken.getId() ) ).thenReturn( refreshToken );
		
		User result = getService().getTokenData( "someToken" );
		assertEquals( refreshToken.getUser(), result );
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
