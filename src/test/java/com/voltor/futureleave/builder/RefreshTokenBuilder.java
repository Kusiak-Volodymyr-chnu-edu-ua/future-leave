package com.voltor.futureleave.builder;

import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.service.DateTimeService;

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

	private void initDefaultData() {
		refreshToken = new RefreshToken();
		Long randomValue = ThreadLocalRandom.current().nextLong(1, 999999);
		refreshToken.setId( randomValue );
		refreshToken.setToken( "SomeToken" + randomValue );
		refreshToken.setExpiringDate( DateTimeService.now().plusDays(2) );
		refreshToken.setSessionId( randomValue );
	}
	
	public static RefreshTokenBuilder start() {
    	return new RefreshTokenBuilder();
	}

	public RefreshTokenBuilder setSessionId( Long sessionId ) {
		refreshToken.setSessionId( sessionId );
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
