package com.voltor.futureleave.dao.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class ActionNotAllowedException extends LocalizedException {

	private static final long serialVersionUID = -8916965781806838888L;

	public ActionNotAllowedException(LocalizedMessage message) {
		super(message);
	}

	public ActionNotAllowedException(String message, LocalizedExceptionCode code) {
		super(new LocalizedMessage(message, code));
	}
}
