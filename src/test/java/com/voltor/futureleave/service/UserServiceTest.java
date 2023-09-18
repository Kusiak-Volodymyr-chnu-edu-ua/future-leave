package com.voltor.futureleave.service;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.voltor.futureleave.builder.AuthenticatedUserBuilder;
import com.voltor.futureleave.builder.UserBuilder;
import com.voltor.futureleave.dao.UserDao;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;

public class UserServiceTest {
	
	@Mock private AuthenticatedUserService userAuthorizationService;
	@Mock private UserDao dao;
	@Mock private SessionService sessionService; 
	@InjectMocks private UserService service;
	
	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.openMocks( this ).close();
		AuthenticatedUserBuilder.start().role( Role.SESSION_USER ).mock( userAuthorizationService );
	}
	
	@Test
	void createOrUpdate() {
		User user = UserBuilder.start().buildNew();
		service.createOrUpdate( user );
		verify( dao, times( 1 ) ).create( user );
		verify( dao, times( 0 ) ).update( eq( user ), anyBoolean() );
		
		user = UserBuilder.start().build();
		when( dao.getOneArchived( eq( user.getId() ), anyBoolean() ) ).thenReturn( user );
		service.createOrUpdate( user );
		verify( dao, times( 0 ) ).create( user );
		verify( dao, times( 1 ) ).update( eq( user ), anyBoolean() );
	}
	
}
