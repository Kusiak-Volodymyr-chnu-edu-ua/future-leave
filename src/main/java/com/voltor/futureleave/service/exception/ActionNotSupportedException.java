package com.voltor.futureleave.service.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class ActionNotSupportedException extends LocalizedException {

	private static final long serialVersionUID = 8997596298377405070L;

	public ActionNotSupportedException() {
		this( "Action not supported" );
	}
	
	public ActionNotSupportedException( String message) {
		super( new LocalizedMessage( message, LocalizedExceptionCode.ACTION_NOT_SUPPORTED) );
	}
}
