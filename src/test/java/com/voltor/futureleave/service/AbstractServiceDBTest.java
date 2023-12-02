package com.voltor.futureleave.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.UserBuilder;
import com.voltor.futureleave.model.Role;

@SpringBootTest
@ActiveProfiles(profiles = {"develop", "h2"})
public class AbstractServiceDBTest {
	
	@Autowired protected UserBuilder userBuilder;
	@Autowired protected AuthenticatedUserService authenticatedUserService;

	@BeforeEach
	void createUser() {
		loginInUser();
	}
	
	void loginInUser() {
		loginInUser( Role.SESSION_USER  );
	}
	
	void loginInUser( Role role ) {
		AuthenticatedUserBuilder.start().role( role).login();
	}
	
	void logoff() {
		SecurityContextHolder.clearContext();
	}

}
