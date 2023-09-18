package com.voltor.futureleave.localization;

public class LocalizedException extends RuntimeException {

	private static final long serialVersionUID = -3420538141424116748L;

	private final LocalizedMessage apiLocalizedMessage;

	public LocalizedException(LocalizedMessage message) {
		super(message.getDefaultDescription());
		this.apiLocalizedMessage = message;
	}

	public LocalizedMessage getApiLocalizedMessage() {
		return apiLocalizedMessage;
	}
}
