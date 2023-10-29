package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.voltor.futureleave.builder.SessionBuilder;
import com.voltor.futureleave.dao.exception.ActionNotAllowedException;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.Session;

class SessionServiceDBTest extends AbstractServiceDBTest{ 
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private SessionBuilder sessionBuilder;
	
	@Test
	public void shouldNotAllowEditingIfCurrentUserIsNotRoot() {
		Session session = sessionBuilder.toDB();
		loginInUser( Role.SESSION_USER  );
		assertFalse( sessionService.editingIsAllowed( session ) );
	} 

	@Test
	public void shouldNotAllowDeleteIfCurrentUserIsNotRoot() {
		loginInUser( Role.SESSION_USER  );
		assertThrows( ActionNotAllowedException.class, () -> sessionService.delete( 1L, true ) );
	}
	
}
