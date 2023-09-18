package com.voltor.futureleave.api.v1.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class ConfirmationTokenExpired extends LocalizedException {

	private static final long serialVersionUID = 554791421690058120L;

	public ConfirmationTokenExpired() {
		super(new LocalizedMessage("Confirmation token was expired",
				LocalizedExceptionCode.CONFIRMATION_TOKEN_WAS_EXPIRED));
	}
}
