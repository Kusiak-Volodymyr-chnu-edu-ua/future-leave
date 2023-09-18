package com.voltor.futureleave.api.v1.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class HttpRequestMethodNotSupportedException extends LocalizedException {

	private static final long serialVersionUID = -8966696170158591612L;

	public HttpRequestMethodNotSupportedException(LocalizedMessage message) {
		super(message);
	}

}
