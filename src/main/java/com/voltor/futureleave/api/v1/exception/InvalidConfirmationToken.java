package com.voltor.futureleave.api.v1.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class InvalidConfirmationToken extends LocalizedException {
 
	private static final long serialVersionUID = 2584200673341253357L;

	public InvalidConfirmationToken() {
        super(new LocalizedMessage("Confirmation token is invalid",
                LocalizedExceptionCode.CONFIRMATION_TOKEN_INVALID));
    }
}
