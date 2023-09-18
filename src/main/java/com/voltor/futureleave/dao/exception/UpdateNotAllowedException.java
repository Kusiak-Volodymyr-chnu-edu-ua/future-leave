package com.voltor.futureleave.dao.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class UpdateNotAllowedException extends LocalizedException {

	private static final long serialVersionUID = -4336390480691164026L;

	public UpdateNotAllowedException(LocalizedMessage message) {
		super(message);
	}

}
