package com.voltor.futureleave.api.v1.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class ObjectNotFoundException extends LocalizedException {

	private static final long serialVersionUID = 4542325733683694129L;

	public ObjectNotFoundException(LocalizedMessage message) {
		super(message);
	}
}