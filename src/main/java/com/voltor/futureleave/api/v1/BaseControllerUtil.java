package com.voltor.futureleave.api.v1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voltor.futureleave.api.v1.exception.LocalizedExceptionUtil;
import com.voltor.futureleave.dao.exception.MissingPropertyException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;

public final class BaseControllerUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BaseControllerUtil.class);

	private BaseControllerUtil() {
	}

	public static <T, ID> T getObjectOrNotFound(T obj, ID id, Class<T> relClass) {
		if (obj == null) {
			throw LocalizedExceptionUtil.buildObjectNotFoundException(relClass, id);
		}
		return obj;
	}

	public static <T, ID> T getObjectOrNotFound(T obj, ID id, Class<T> relClass, Class<?> onClass) {
		if (obj == null) {
			final String message = String.format("Failed to link %s (ID=%s) on %s", relClass.getSimpleName(), id, onClass.getSimpleName());
			LOG.warn(message);
			throw LocalizedExceptionUtil.buildObjectNotFoundException(relClass, id, message);
		}
		return obj;
	}
	
	public static List<String> parseExpandField(Optional<String> expandFields) {
		return expandFields.map(s -> Arrays.asList(s.split(","))).orElse(Collections.emptyList());
	}

	public static void checkPatchRequest(Map<String, Object> request) {
		if (request.isEmpty()) {
			throw new MissingPropertyException(new LocalizedMessage(
					"Request object on PATCH is empty.",
					LocalizedExceptionCode.MISSING_PROPERTY_EXCEPTION_PATCH_BODY));
		}
	}
}
