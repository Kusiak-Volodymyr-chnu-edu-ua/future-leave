package com.voltor.futureleave.service.exception;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class ParseDatePatternException extends LocalizedException {
 
	private static final long serialVersionUID = -5077710706136760560L;

	public ParseDatePatternException( String message ) {
		super( new LocalizedMessage( message, LocalizedExceptionCode.DATE_PATTERN_PARSE_ERROR ) );
	}
}
