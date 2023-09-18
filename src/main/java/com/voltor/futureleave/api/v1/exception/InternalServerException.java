package com.voltor.futureleave.api.v1.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class InternalServerException extends LocalizedException {

	private static final long serialVersionUID = -7370199638284678050L;

	public InternalServerException(LocalizedMessage message) {
		super(message);
	}

}
