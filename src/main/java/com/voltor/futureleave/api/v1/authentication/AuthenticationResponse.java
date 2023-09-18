package com.voltor.futureleave.api.v1.authentication;

public class AuthenticationResponse {

	private String accessToken;
	private String refreshToken;
	private int accessTokenLifetimeMinutes;
	private int refreshTokenLifetimeMinutes;

	public AuthenticationResponse(String accessToken, String refreshToken, int accessTokenLifetimeMinutes, int refreshTokenLifetimeMinutes) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenLifetimeMinutes = accessTokenLifetimeMinutes;
		this.refreshTokenLifetimeMinutes = refreshTokenLifetimeMinutes;
	}

	public int getAccessTokenLifetimeMinutes() {
		return accessTokenLifetimeMinutes;
	}

	public void setAccessTokenLifetimeMinutes( int accessTokenLifetimeMinutes ) {
		this.accessTokenLifetimeMinutes = accessTokenLifetimeMinutes;
	}

	public int getRefreshTokenLifetimeMinutes() {
		return refreshTokenLifetimeMinutes;
	}

	public void setRefreshTokenLifetimeMinutes( int refreshTokenLifetimeMinutes ) {
		this.refreshTokenLifetimeMinutes = refreshTokenLifetimeMinutes;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
