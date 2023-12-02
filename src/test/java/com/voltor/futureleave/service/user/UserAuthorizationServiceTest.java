package com.voltor.futureleave.service.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.service.AuthenticatedUserService;
import com.voltor.futureleave.service.exception.NoCurrentUserException;

@SpringBootTest
@ActiveProfiles(profiles = { "develop" })
public class UserAuthorizationServiceTest {

	@Autowired
	private AuthenticatedUserService userAuthorizationService;

	@MockBean
	private SecurityContext securityContext;

	@MockBean
	private PermissionEvaluator permissionEvaluator;

	@MockBean
	private Authentication authentication;
	
	@BeforeEach
	public void setup() {
		SecurityContextHolder.setContext( securityContext );
		given( securityContext.getAuthentication() ).willReturn( authentication );
	}

	@Test
	public void noAuthenticationTest() {
		given( securityContext.getAuthentication() ).willReturn( null );
		assertThrows( NoCurrentUserException.class, () -> userAuthorizationService.getCurrentUser() );
	}

	@Test
	public void wrongAuthenticationTest() {
		Object objectWithWrongPrincipalType = new Object();
		given( securityContext.getAuthentication().getPrincipal() ).willReturn( objectWithWrongPrincipalType );
		assertThrows( NoCurrentUserException.class, () -> userAuthorizationService.getCurrentUser() );
	}

	@Test
	public void rightAuthenticationTest() {
		AuthenticatedUser build = AuthenticatedUserBuilder.start().build();
		given( authentication.getPrincipal() ).willReturn( build );
		assertNotNull( userAuthorizationService.getCurrentUser() );
	}

	@Test
	public void rootRoleTest() {
		AuthenticatedUser build = AuthenticatedUserBuilder.start().role( Role.ROOT ).build();
		given( authentication.getPrincipal() ).willReturn( build );
		assertTrue( userAuthorizationService.isRoot() );
	}

	@Test
	public void supportRoleTest() {
		AuthenticatedUser build = AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).build();
		given( authentication.getPrincipal() ).willReturn( build );
		assertTrue( userAuthorizationService.isSupport() );
	}
}
