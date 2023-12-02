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

import com.voltor.futureleave.builder.UserBuilder;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.security.UserDetailsServiceImpl;
import com.voltor.futureleave.service.UserService;

public class UserDetailsServiceTest {
	
	@Mock
	private UserService userService;
	 
	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;
	
	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks(this).close();
		userDetailsService = new UserDetailsServiceImpl( userService );
	}

	@Test
	public void testSessionUserRoleAuthority() {
		User user = UserBuilder.start().build();
		given( userService.findByLogin( anyString() ) ).willReturn( user );

		AuthenticatedUser ud = (AuthenticatedUser) userDetailsService.loadUserByUsername( user.getLogin() );
		 
		
		assertEquals( Role.SESSION_USER, ud.getRole() );

		assertEquals( user, ud.getUser() );

		assertTrue( ud.getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_SESSION_USER" ) ) );
	}
}
