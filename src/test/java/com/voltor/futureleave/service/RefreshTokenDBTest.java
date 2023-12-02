package com.voltor.futureleave.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.voltor.futureleave.builder.RefreshTokenBuilder;
import com.voltor.futureleave.model.RefreshToken;

class RefreshTokenDBTest extends AbstractServiceDBTest{
	 
	@Autowired
	private RefreshTokenService refreshTokenService;
	 
	@Autowired
	private RefreshTokenBuilder refreshTokenBuilder;

	@Test
	public void shouldThrowExceptionDueToConstraint() {
		RefreshToken token = refreshTokenBuilder.buildNew();
		refreshTokenService.create(token);
		RefreshToken secondToken = refreshTokenBuilder.setRefreshToken( token.getToken() ).buildNew();
		Exception exception = assertThrows(DataIntegrityViolationException.class,
				() -> refreshTokenService.create(secondToken));
		assertNotNull(exception);
	}

}
