package com.voltor.futureleave.service.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class RefreshTokenNotFoundException extends LocalizedException {
 
	private static final long serialVersionUID = -924636145661420942L;

	public RefreshTokenNotFoundException() {
		super(new LocalizedMessage("Refresh token has not found",
				LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION));
	}
}
