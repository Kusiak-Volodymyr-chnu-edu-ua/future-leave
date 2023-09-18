package com.voltor.futureleave.security.jwt.exception;


public class JwtExpirationException extends RuntimeException {

	private static final long serialVersionUID = 5698033833517025332L;

	public JwtExpirationException(String message) {
		super(message);
	}
}
