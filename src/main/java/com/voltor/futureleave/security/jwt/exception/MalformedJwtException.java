package com.voltor.futureleave.security.jwt.exception;


public class MalformedJwtException extends RuntimeException {

	private static final long serialVersionUID = -8457988314038988894L;

	public MalformedJwtException(String message) {
		super(message);
	}
}
