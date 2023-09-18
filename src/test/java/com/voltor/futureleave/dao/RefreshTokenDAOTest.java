package com.voltor.futureleave.dao;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.voltor.futureleave.builder.RefreshTokenBuilder;
import com.voltor.futureleave.dao.shared.AbstractIdentifiableDaoTest;
import com.voltor.futureleave.jpa.RefreshTokenRepository;
import com.voltor.futureleave.model.RefreshToken;

public class RefreshTokenDAOTest extends AbstractIdentifiableDaoTest<RefreshToken, RefreshTokenDao, RefreshTokenRepository> {

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	private RefreshTokenBuilder refreshTokenBuilder = new RefreshTokenBuilder();

	@Override
	public RefreshToken createEntity() {
		return refreshTokenBuilder.build();
	}

	@Override
	public RefreshTokenDao createDao() {
		return new RefreshTokenDao(userAuthorizationService, refreshTokenRepository);
	}

	@Override
	public RefreshTokenRepository createRepository() {
		return refreshTokenRepository;
	}

	@Disabled("Not supported")
	@Test
	@Override
	public void testRootAbleToEdit() {
		fail();
	}
}
