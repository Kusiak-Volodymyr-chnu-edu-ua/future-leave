package com.voltor.futureleave.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.voltor.futureleave.builder.AuthDataBuilder;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.security.UserDetailsServiceImpl;
import com.voltor.futureleave.service.AuthDataService;

public class UserDetailsServiceTest {
	
	@Mock
	private AuthDataService authDataService;
	 
	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;
	
	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks(this).close();
		userDetailsService = new UserDetailsServiceImpl( authDataService );
	}

	@Test
	public void testSessionUserRoleAuthority() {
		AuthData authData = AuthDataBuilder.start().build();
		given( authDataService.findByClientId( anyString() ) ).willReturn( authData );

		AuthenticatedUser ud = (AuthenticatedUser) userDetailsService.loadUserByUsername( authData.getClientId() );
		 
		
		assertEquals( Role.SESSION_USER, ud.getRole() );

		assertEquals( authData.getSessionId(), ud.getSessionId() );
		assertEquals( authData.getClientId(), ud.getClientId() ); 

		assertTrue( ud.getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_SESSION_USER" ) ) );
	}
}
