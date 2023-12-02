package com.voltor.futureleave.api.v1;

public class ApiConstants {

	private ApiConstants() {
	}

	// API
	public static final String API_PREFIX = "/api";
	public static final String API_VERSION_V1 = "/v1";

	// Endpoints
	public static final String AUTHENTICATION_ENDPOINT = "/login";
	public static final String REFRESH_TOKEN_ENDPOINT = "/refresh";
	public static final String V1_SESSION_ENDPOINT = API_PREFIX + API_VERSION_V1 + "/sessions";
	public static final String V1_PERIOD_ENDPOINT = API_PREFIX + API_VERSION_V1 + "/periods";
}
