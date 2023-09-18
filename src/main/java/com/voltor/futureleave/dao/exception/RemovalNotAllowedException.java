package com.voltor.futureleave.dao.exception;

import java.util.List;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class RemovalNotAllowedException extends LocalizedException {

	private static final long serialVersionUID = 8358003983992258944L;

	public RemovalNotAllowedException(LocalizedMessage message) {
		super(message);
	}

	public RemovalNotAllowedException(LocalizedMessage message, List<LocalizedMessage> details) {
		super(message);
	}

}
