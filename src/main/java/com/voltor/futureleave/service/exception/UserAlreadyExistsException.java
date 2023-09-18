package com.voltor.futureleave.service.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class UserAlreadyExistsException extends LocalizedException {

	private static final long serialVersionUID = 7889505965675984160L;

	public UserAlreadyExistsException(LocalizedMessage message) {
		super(message);
	}
}
