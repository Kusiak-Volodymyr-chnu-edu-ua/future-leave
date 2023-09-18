package com.voltor.futureleave.builder;

import java.util.concurrent.ThreadLocalRandom;

import com.voltor.futureleave.model.AuthData;

public class AuthDataBuilder {
	
	private AuthData authData;
	
	public AuthDataBuilder() {
		initDefaultData();
	}
	
	public AuthData build() {
    	AuthData authData = this.authData;
		initDefaultData();
		return authData;
	}
	
	public static AuthDataBuilder start() {
    	return new AuthDataBuilder();
	}

	private void initDefaultData() {
		authData = new AuthData();
		Long randomValue = ThreadLocalRandom.current().nextLong(1, 9999999);
		authData.setClientId( "client-id" + randomValue );
		authData.setClientSecretKey( "client-secret" + randomValue );
		authData.setSessionId(  randomValue );
	}
	
	public AuthDataBuilder setSessionId( Long sessionId ) {
		authData.setSessionId( sessionId );
		return this;		
	}
	
	public AuthDataBuilder id( Long id ) {
		authData.setId( id );
		return this;		
	}
	
	public AuthDataBuilder clientSecret( String clientSecret ) {
		authData.setClientSecretKey( clientSecret );
		return this;		
	}
	
}
