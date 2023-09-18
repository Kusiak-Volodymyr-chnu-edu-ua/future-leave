package com.voltor.futureleave.repository.token;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import com.voltor.futureleave.builder.RefreshTokenBuilder;
import com.voltor.futureleave.jpa.RefreshTokenRepository;
import com.voltor.futureleave.model.RefreshToken;

@DataJpaTest
@ActiveProfiles(profiles = {"develop", "h2"})
public class RefreshTokenRepositoryTest {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	private RefreshTokenBuilder refreshTokenBuilder = new RefreshTokenBuilder();

	@Test
	public void shouldThrowExceptionDueToConstraint() {
		RefreshToken token = refreshTokenBuilder.build();
		refreshTokenRepository.save(token);
		Exception exception = assertThrows(DataIntegrityViolationException.class,
				() -> refreshTokenRepository.save(token));
		assertNotNull(exception);
	}

}