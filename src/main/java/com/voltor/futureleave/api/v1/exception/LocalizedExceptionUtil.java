package com.voltor.futureleave.api.v1.exception;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public class LocalizedExceptionUtil {

	private LocalizedExceptionUtil() {
	}

	public static HttpRequestMethodNotSupportedException buildHttpRequestMethodNotSupportedException(String method) {

		final String defaultErrorMessage = String.format("%s method is not supported.", method);

		final HashMap<String, String> messageArguments = new HashMap<>();
		messageArguments.put("method", method);

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage, LocalizedExceptionCode.METHOD_NOT_SUPPORTED_MESSAGE);

		message.setMessageArguments(messageArguments);

		return new HttpRequestMethodNotSupportedException(message);
	}

	public static ObjectNotFoundException buildObjectsNotFoundException(
			Class<?> relClass, List<Long> values, String detailMessage) {
		String valueDetails = null;

		if( values != null  ){
			values.sort(Comparator.comparingLong( item -> item ));
			valueDetails =  StringUtils.join(values, ", ");
		}
		return buildObjectNotFoundException(relClass, valueDetails, detailMessage);
	}

	public static ObjectNotFoundException buildObjectNotFoundException(
			Class<?> relClass, Object id, String detailMessage) {
		String valueDetails =  id == null ? null : id.toString();
		return buildObjectNotFoundException(relClass, valueDetails, detailMessage);
	}

	public static ObjectNotFoundException buildObjectNotFoundException(
			Class<?> relClass, String valueDetails, String detailMessage) {

		final String defaultMessage = String.format("Requested object (%s) with identifier (%s) was not found. %s",
				relClass.getSimpleName(), valueDetails, detailMessage != null ? detailMessage : "");
		final HashMap<String, String> messageArguments = new HashMap<>();
		messageArguments.put("identifier", valueDetails);
		final HashMap<String, String> localizedMessageArguments = new HashMap<>();
		localizedMessageArguments.put("entity", relClass.getSimpleName().toUpperCase());
		final LocalizedMessage localizedMessage = new LocalizedMessage(defaultMessage, LocalizedExceptionCode.OBJECT_NOT_FOUND_EXCEPTION);
		localizedMessage.setLocalizedMessageArguments(localizedMessageArguments);
		localizedMessage.setMessageArguments(messageArguments);

		return new ObjectNotFoundException(localizedMessage);
	}

	public static ObjectNotFoundException buildObjectNotFoundException(Class<?> relClass, Object id) {
		return buildObjectNotFoundException(relClass, id, null);
	}

}
