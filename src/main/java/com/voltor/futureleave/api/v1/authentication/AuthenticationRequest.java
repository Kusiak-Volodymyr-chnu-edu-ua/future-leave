package com.voltor.futureleave.api.v1.authentication;

public class AuthenticationRequest {

	private String clientId;
	private String clientSecret;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId( String clientId ) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret( String clientSecret ) {
		this.clientSecret = clientSecret;
	}

}
