package com.voltor.futureleave.dao.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class InvalidPropertyException extends LocalizedException {

	private static final long serialVersionUID = -1364516259963877038L;

	public InvalidPropertyException(LocalizedMessage message) {
		super(message);
	}

}
