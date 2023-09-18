package com.voltor.futureleave.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.SessionBuilder;
import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.model.Role;

@SpringBootTest
@ActiveProfiles(profiles = {"develop", "h2"})
public class AbstractServiceDBTest {
	
	@Autowired protected SessionBuilder sessionBuilder;

	@BeforeEach
	void createSession() {
		loginInNewSession();
	}
	
	void loginInNewSession() {
		AuthenticatedUserBuilder.start().role( Role.ROOT ).login();
		Session session = sessionBuilder.initDefaultDBRelations().toDB();
		AuthenticatedUserBuilder.start().sessionId( session.getSessionId() ).role( Role.SESSION_USER ).login();
	}

}
