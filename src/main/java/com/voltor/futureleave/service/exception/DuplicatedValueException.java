package com.voltor.futureleave.service.exception;

public class DuplicatedValueException extends RuntimeException {

	private static final long serialVersionUID = -3265347875304246742L;

	public DuplicatedValueException(String message) {
		super(message);
	}
}
