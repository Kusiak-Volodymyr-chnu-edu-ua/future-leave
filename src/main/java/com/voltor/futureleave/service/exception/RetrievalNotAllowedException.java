package com.voltor.futureleave.service.exception;

import java.util.List;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedMessage;

public class RetrievalNotAllowedException extends LocalizedException {

	private static final long serialVersionUID = 1371644362245531832L;

	public RetrievalNotAllowedException(LocalizedMessage message) {
		super(message);
	}

	public RetrievalNotAllowedException(LocalizedMessage message, List<LocalizedMessage> details) {
		super(message);
	}
}
