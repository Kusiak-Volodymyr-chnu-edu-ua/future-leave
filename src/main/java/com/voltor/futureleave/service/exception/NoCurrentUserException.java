package com.voltor.futureleave.service.exception;

import org.springframework.security.core.AuthenticationException;

public class NoCurrentUserException extends AuthenticationException {
    
	private static final long serialVersionUID = -8436317507381086898L;

	public NoCurrentUserException() {
        super("No current user could be found.");
    }
}
