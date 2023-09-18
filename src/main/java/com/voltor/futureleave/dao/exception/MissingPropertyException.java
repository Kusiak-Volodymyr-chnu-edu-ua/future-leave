package com.voltor.futureleave.dao.exception;

import java.util.List;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class MissingPropertyException extends LocalizedException {

	private static final long serialVersionUID = 7930091992480168620L;

	public MissingPropertyException(LocalizedMessage message) {
		super(message);
	}

	public MissingPropertyException(LocalizedMessage message, List<LocalizedMessage> details) {
		super(message);
	}

}
