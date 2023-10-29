package com.voltor.futureleave.builder;

import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.DateTimeService;

@Component
public class RefreshTokenBuilder {

	private RefreshToken refreshToken;

	public RefreshTokenBuilder() {
		initDefaultData();
	}

	public RefreshToken build() {
		RefreshToken token = this.refreshToken;
		initDefaultData();
		return token;
	}
	
	public RefreshToken buildNew() {
		return setId(null).build();
	}

	private void initDefaultData() {
		refreshToken = new RefreshToken();
		Long randomValue = ThreadLocalRandom.current().nextLong(1, 999999);
		refreshToken.setId( randomValue );
		refreshToken.setToken( "SomeToken" + randomValue );
		refreshToken.setExpiringDate( DateTimeService.now().plusDays(2) );
		refreshToken.setUser( UserBuilder.start().build() );
	}
	
	public static RefreshTokenBuilder start() {
    	return new RefreshTokenBuilder();
	}
	
	public RefreshTokenBuilder setId( Long id ) {
		refreshToken.setId( id );
		return this;
	}
	
	public RefreshTokenBuilder setUser( User user ) {
		refreshToken.setUser( user );
		return this;
	}

	public RefreshTokenBuilder setRefreshToken(String tokenValue) {
		refreshToken.setToken( tokenValue );
		return this;
	}

	public RefreshTokenBuilder setExpiringDate(ZonedDateTime expiringDate) {
		refreshToken.setExpiringDate( expiringDate );
		return this;
	}
}
