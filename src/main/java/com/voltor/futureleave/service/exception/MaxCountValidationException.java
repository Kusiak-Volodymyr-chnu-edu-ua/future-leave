package com.voltor.futureleave.service.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class MaxCountValidationException extends LocalizedException {

	private static final long serialVersionUID = -4432148641551430162L;

	public MaxCountValidationException(LocalizedMessage message) {
		super(message);
	}
}
