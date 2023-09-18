package com.voltor.futureleave.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.voltor.futureleave.service.RefreshTokenService;

@SpringBootTest
@ActiveProfiles(profiles = {"develop", "h2"})
public class JwtServiceTest {

	@MockBean
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtService jwtService;

	@Test
	public void shouldSaveRefreshToken() {
		String token = jwtService.generateRefreshToken();
		assertNotNull(token);
		verify(refreshTokenService).create(any(String.class), any(ZonedDateTime.class));
	}
}
