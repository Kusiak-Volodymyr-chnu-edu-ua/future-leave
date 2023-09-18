package com.voltor.futureleave.api.v1.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class LocalizedApiError {

	private final HttpStatusCode status;

	private final String errorClass;

	private final LocalizedMessage title;

	public LocalizedApiError(
			LocalizedMessage title,
			HttpStatusCode status,
			String errorClass) {
		this.status = status;
		this.title = title;
		this.errorClass = errorClass;
	}

	public LocalizedApiError(Throwable exception, HttpStatus status) {
		this.status = status;
		if (exception instanceof LocalizedException) {
			final LocalizedException ex = (LocalizedException) exception;
			title = ex.getApiLocalizedMessage();
			errorClass = ex.getClass().getSimpleName();
		} else {
			final Throwable ex = Objects.requireNonNull(exception);
			title = new LocalizedMessage(ex.getMessage(), LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION);
			errorClass = ex.getClass().getSimpleName();
		}
	}

	public HttpStatusCode getStatus() {
		return status;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public LocalizedMessage getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "ApiErrorLocalized [status=" + status + ", errorClass=" + errorClass + ", title=" + title + ", "
				+ "]";
	}

}
