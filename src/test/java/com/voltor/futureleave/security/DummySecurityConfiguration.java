package com.voltor.futureleave.security;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.voltor.futureleave.dao.SessionDao;
import com.voltor.futureleave.security.jwt.JwtService;

@TestConfiguration
public class DummySecurityConfiguration {

	@Bean
	public UserDetailsService userDetailsService() {
		return Mockito.mock(UserDetailsServiceImpl.class);
	}

	@Bean
	public JwtService jwtService() {
		return Mockito.mock(JwtService.class);
	}

	@Bean
	public SessionDao sessionDao() {
		return Mockito.mock(SessionDao.class);
	}

}
