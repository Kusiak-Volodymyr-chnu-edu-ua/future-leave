package com.voltor.futureleave.dao.exception;

import static com.voltor.futureleave.localization.LocalizedExceptionCode.MISSING_PROPERTY_EXCEPTION_FOR_MESSAGE;
import static com.voltor.futureleave.localization.LocalizedExceptionCode.REMOVAL_NOT_ALLOWED_EXCEPTION_DETAILS_PERMISSION;
import static com.voltor.futureleave.localization.LocalizedExceptionCode.REMOVAL_NOT_ALLOWED_EXCEPTION_MESSAGE;
import static com.voltor.futureleave.localization.LocalizedExceptionCode.UPDATE_NOT_ALLOWED_EXCEPTION_DETAILS_PERMISSION;
import static com.voltor.futureleave.localization.LocalizedExceptionCode.UPDATE_NOT_ALLOWED_EXCEPTION_MESSAGE;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.voltor.futureleave.localization.LocalizedMessage;
import com.voltor.futureleave.model.PrimaryEntity;

public class LocalizationUtils {

	private LocalizationUtils() {
	}

	public static MissingPropertyException buildMissingPropertyException(
			String property,
			Class<?> onObjectClass,
			LocalizedMessage... details) {
		final String defaultErrorMessage = String.format("Mandatory property [%s] is missing for %s.",
				property, onObjectClass.getSimpleName());

		final Map<String, String> messageArguments = new HashMap<>();
		messageArguments.put("property", property);

		final Map<String, String> localizedArguments = new HashMap<>();
		localizedArguments.put("entity", onObjectClass.getSimpleName().toUpperCase());

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage,
				MISSING_PROPERTY_EXCEPTION_FOR_MESSAGE);
		message.setMessageArguments(messageArguments);
		message.setLocalizedMessageArguments(localizedArguments);

		return new MissingPropertyException(message, details == null ? null : Arrays.asList(details));
	}

	public static RemovalNotAllowedException buildRemovalNotAllowedException(
			PrimaryEntity<?> entity,
			LocalizedMessage... details) {
		final String nameOrId = getEntityNameOrId(entity);
		final String defaultErrorMessage = String.format(
				"Removing of %s(%s) is not allowed.",
				entity.getClass().getSimpleName(), nameOrId);

		final Map<String, String> messageArguments = new HashMap<>();
		messageArguments.put("name", nameOrId);

		final Map<String, String> localizedArguments = new HashMap<>();
		localizedArguments.put("entity", entity.getClass().getSimpleName().toUpperCase());

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage,
				REMOVAL_NOT_ALLOWED_EXCEPTION_MESSAGE);
		message.setMessageArguments(messageArguments);
		message.setLocalizedMessageArguments(localizedArguments);

		return new RemovalNotAllowedException(message, details == null ? null : Arrays.asList(details));
	}

	public static RemovalNotAllowedException buildRemovalNotAllowedExceptionNoPermission(PrimaryEntity<?> entity) {
		return buildRemovalNotAllowedException(entity, new LocalizedMessage(
				"Current user has no permission.",
				REMOVAL_NOT_ALLOWED_EXCEPTION_DETAILS_PERMISSION));
	}

	public static UpdateNotAllowedException buildUpdateNotAllowedException(
			PrimaryEntity<?> entity,
			LocalizedMessage... details) {
		final String nameOrId = getEntityNameOrId(entity);
		final String defaultErrorMessage = String.format(
				"Editing of %s(%s) is not allowed.",
				entity.getClass().getSimpleName(), nameOrId);

		final Map<String, String> messageArguments = new HashMap<>();
		messageArguments.put("name", nameOrId);

		final Map<String, String> localizedArguments = new HashMap<>();
		localizedArguments.put("entity", entity.getClass().getSimpleName().toUpperCase());

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage,
				UPDATE_NOT_ALLOWED_EXCEPTION_MESSAGE);

		message.setMessageArguments(messageArguments);
		message.setLocalizedMessageArguments(localizedArguments);

		return new UpdateNotAllowedException(message);
	}

	public static UpdateNotAllowedException buildUpdateNotAllowedExceptionNoPermission(PrimaryEntity<?> entity) {
		return buildUpdateNotAllowedException(entity, new LocalizedMessage(
				"Current user has no permission.",
				UPDATE_NOT_ALLOWED_EXCEPTION_DETAILS_PERMISSION));
	}

	public static String getEntityNameOrId(PrimaryEntity<?> entity) {
		final Method m = ReflectionUtils.findMethod(entity.getClass(), "getName");
		if (m != null) {
			return (String) ReflectionUtils.invokeMethod(m, entity);
		} else {
			return entity.getId() == null ? "-" : entity.getId().toString();
		}
	}
}
