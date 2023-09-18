package com.voltor.futureleave.security.jwt.exception;


public class JwtBadSignatureException extends RuntimeException {

	private static final long serialVersionUID = -6430382606508226312L;

	public JwtBadSignatureException(String message) {
		super(message);
	}
}
