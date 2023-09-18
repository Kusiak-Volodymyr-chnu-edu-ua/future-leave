package com.voltor.futureleave.service.exception;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;
import com.voltor.futureleave.model.Identifiable;

public class LocalizationUtils {

	private LocalizationUtils() {
	}

	public static UserAlreadyExistsException buildUserAlreadyExistsException(String emailAddress) {
		final String defaultErrorMessage = String.format("User with email address (%s) already exists.", emailAddress);

		final Map<String, String> messageArguments = new HashMap<>();
		messageArguments.put("email", emailAddress);

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage, LocalizedExceptionCode.USER_EXISTS_EXCEPTION);
		message.setMessageArguments(messageArguments);
		return new UserAlreadyExistsException(message);
	}

	public static RetrievalNotAllowedException buildRetrievalNotAllowedException(
			Class<?> entityClass, LocalizedMessage... details) {
		final String defaultErrorMessage = String.format("Current user is not allowed to retrieve %s.",
				entityClass.getSimpleName());

		final Map<String, String> localizedArguments = new HashMap<>();
		localizedArguments.put("entity", entityClass.getSimpleName().toUpperCase());

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage,
				LocalizedExceptionCode.RETRIEVAL_NOT_ALLOWED_EXCEPTION_MESSAGE);
		message.setLocalizedMessageArguments(localizedArguments);
		return new RetrievalNotAllowedException(message, details == null ? null : Arrays.asList(details));
	}

	public static ConstraintValidationException buildConstraintValidationExceptionMissingRequiredField(
			Class<?> entityClass, String fieldName, LocalizedMessage... details) {
		final String defaultErrorMessage = String.format("[%s] field is mandatory for %s.", fieldName,
				entityClass.getSimpleName());

		final Map<String, String> messageArguments = new HashMap<>();
		messageArguments.put("fieldName", fieldName);

		final Map<String, String> localizedArguments = new HashMap<>();
		localizedArguments.put("entity", entityClass.getSimpleName().toUpperCase());

		final LocalizedMessage message = new LocalizedMessage(defaultErrorMessage,
				LocalizedExceptionCode.CONSTRAINT_VALIDATION_EXCEPTION_MISSING_REQUIRED_FIELD);

		message.setLocalizedMessageArguments(localizedArguments);
		message.setMessageArguments(messageArguments);

		return new ConstraintValidationException(message);
	}


	public static String getEntityNameOrId(Identifiable entity) {
		Method m = ReflectionUtils.findMethod(entity.getClass(), "getName");
		if (m != null) {
			return (String) ReflectionUtils.invokeMethod(m, entity);
		} else {
			return entity.getId() == null ? "-" : entity.getId().toString();
		}
	}

}
