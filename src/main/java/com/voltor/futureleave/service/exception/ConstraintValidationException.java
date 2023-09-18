package com.voltor.futureleave.service.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class ConstraintValidationException extends LocalizedException {

	private static final long serialVersionUID = 8997596298377405070L;

	public ConstraintValidationException(LocalizedMessage message) {
		super(message);
	}
}
