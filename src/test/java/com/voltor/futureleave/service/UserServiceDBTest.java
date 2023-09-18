package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.voltor.futureleave.builder.UserBuilder;
import com.voltor.futureleave.model.User;

class UserServiceDBTest extends AbstractServiceDBTest{
	
	@Autowired private UserService userService;
	@Autowired private UserBuilder userBuilder;
	
	@Test
	void encodePasswordOnCreate() {
		User user = userBuilder.buildNew();
		String password = user.getPassword();
		user = userService.create( user );
		assertTrue( EncryptingUtils.checkPassword( password, user.getPassword() ) );
	}
	
	@Test
	void encodePasswordOnUndate() {
		User user = userBuilder.toDB();
		String oldEncriptedPassword = user.getPassword();
		String newPassword = "bala bla test";
		user.setPassword( newPassword );
		user = userService.update( user );
		assertFalse( EncryptingUtils.checkPassword( newPassword, oldEncriptedPassword ) );
		assertTrue( EncryptingUtils.checkPassword( newPassword, user.getPassword() ) );
	}
	
	@Test
	void dontEncodePasswordOnUndate() {
		User user = userBuilder.buildNew();
		String password = user.getPassword();
		user = userService.create( user );
		String oldEncriptedPassword = user.getPassword();
		user = userService.update( user );
		assertEquals( oldEncriptedPassword, user.getPassword() );
		assertTrue( EncryptingUtils.checkPassword( password, oldEncriptedPassword ) );
		assertTrue( EncryptingUtils.checkPassword( password, user.getPassword() ) );
	}
	
	@Test
	void checkloginUnique() {
		User user = userBuilder.toDB();
		assertTrue( userService.isLoginUnique( user ) );
		user.setId( 0L );
		assertFalse( userService.isLoginUnique( user ) );
	}


}
