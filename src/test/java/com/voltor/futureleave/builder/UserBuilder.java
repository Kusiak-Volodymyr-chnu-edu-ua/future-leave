package com.voltor.futureleave.builder;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.UserService;

@Component
public class UserBuilder {

	@Autowired private UserService userService;
	
	private User user;

	public UserBuilder() {
		initDefaultData();
	}
	
	public static UserBuilder start() {
    	return new UserBuilder();
	}

	public User build() {
		User entity = this.user;
		initDefaultData();
		return entity;
	}

	public User buildNew() {
		return setId(null).build();
	}
	
	//Spring based
	public User toDB() {
		User user = buildNew();
		return userService.create( user );
	}

	private void initDefaultData() {
		Long randomValue = ThreadLocalRandom.current().nextLong(1, 999999);
		user = new User();
		setId( randomValue );
		setLastName( "LastName_" + randomValue );
		setEmail( "Email_" + randomValue + "@test.test" );
		setFirstName( "FirstName_" + randomValue );
		setLogin( "Login_" + randomValue );
		setPassword( "Password_" + randomValue );
		setRole( Role.SESSION_USER );
	}

	public UserBuilder setId( Long id ) {
		user.setId( id );
		return this;
	}

	public UserBuilder setLastName( String lastName ) {
		user.setLastName( lastName );
		return this;
	}
	
	public UserBuilder setFirstName( String firstName ) {
		user.setFirstName( firstName );
		return this;
	}

	public UserBuilder setEmail( String email ) {
		user.setEmail( email );
		return this;
	}
	
	public UserBuilder setLogin( String login ) {
		user.setLogin( login );
		return this;
	}
	
	public UserBuilder setRole( Role role ) {
		user.setUserRole( role );
		return this;
	}
	
	public UserBuilder setPassword( String password ) {
		user.setPassword( password );
		return this;
	}

}
