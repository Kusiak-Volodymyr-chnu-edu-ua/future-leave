package com.voltor.futureleave.localization;

public enum LocalizedExceptionCode implements ILocalizedExceptionCode {

	ERROR_OCCURRED_EXCEPTION("EXCEPTION.ERROR_OCCURRED"),

	OBJECT_NOT_FOUND_EXCEPTION("EXCEPTION.OBJECT_NOT_FOUND"),
	OBJECT_NOT_FOUND_GENERAL_EXCEPTION("EXCEPTION.OBJECT_NOT_FOUND_GENERAL"),

	USER_EXISTS_EXCEPTION("EXCEPTION.USER_EXISTS"),

	REMOVAL_VERIFICATION_WARNING_MESSAGE("REMOVAL.WARNING.MESSAGE"),
	REMOVAL_VERIFICATION_WARNING_DETAIL("REMOVAL.WARNING.DETAIL"),
	REMOVAL_VERIFICATION_WARNING_DETAILS("REMOVAL.WARNING.DETAILS"),
	REMOVAL_VERIFICATION_ERROR_MESSAGE("REMOVAL.ERROR.MESSAGE"),
	REMOVAL_VERIFICATION_ARCHIVED("EXCEPTION.REMOVAL_ARCHIVED"),

	METHOD_NOT_SUPPORTED_MESSAGE("METHOD_NOT_SUPPORTED.MESSAGE"),

	PASSWORD_MISMATCH_MESSAGE("PASSWORD_MISMATCH"),

	RETRIEVAL_NOT_ALLOWED_EXCEPTION_MESSAGE("EXCEPTION.RETRIEVAL_NOT_ALLOWED.MESSAGE"),

	REMOVAL_ALREADY_USED_EXERCISE("EXCEPTION.REMOVAL_ALREADY_USED_EXERCISE"),

	REMOVAL_NOT_ALLOWED_EXCEPTION_MESSAGE("EXCEPTION.REMOVAL_NOT_ALLOWED.MESSAGE"),
	REMOVAL_NOT_ALLOWED_EXCEPTION_DETAILS_PERMISSION("EXCEPTION.REMOVAL_NOT_ALLOWED.DETAILS.PERMISSION"),

	UPDATE_NOT_ALLOWED_EXCEPTION_MESSAGE("EXCEPTION.UPDATE_NOT_ALLOWED.MESSAGE"),
	UPDATE_NOT_ALLOWED_EXCEPTION_DETAILS_PERMISSION("EXCEPTION.UPDATE_NOT_ALLOWED.DETAILS.PERMISSION"),

	ACTION_NOT_ALLOWED_EXCEPTION_CREATE("EXCEPTION.ACTION_NOT_ALLOWED.CREATE"),

	CONSTRAINT_VALIDATION_EXCEPTION_MISSING_REQUIRED_FIELD("EXCEPTION.CONSTRAINT_VALIDATION_EXCEPTION.MISSING_REQUIRED_FIELD"),
	CONSTRAINT_VALIDATION_EXCEPTION("EXCEPTION.CONSTRAINT_VALIDATION_EXCEPTION"),

	MISSING_PROPERTY_EXCEPTION_MESSAGE("EXCEPTION.MISSING_PROPERTY.MESSAGE"),
	MISSING_PROPERTY_EXCEPTION_FOR_MESSAGE("EXCEPTION.MISSING_PROPERTY.FOR_MESSAGE"),
	MISSING_PROPERTY_EXCEPTION_PATCH_BODY("EXCEPTION.MISSING_PROPERTY.PATCH_BODY"),

	INVALID_PROPERTY_EXCEPTION_FOR_MESSAGE("EXCEPTION.INVALID_PROPERTY.FOR_MESSAGE"),
	INVALID_PROPERTY_EXCEPTION_CHANGE_IS_NOT_ALLOWED("EXCEPTION.INVALID_PROPERTY.CHANGE_NOT_ALLOWED"),
	INVALID_PROPERTY_EXCEPTION_USER_PASSWORD_LENGTH("EXCEPTION.INVALID_PROPERTY.USER.PASSWORD_LENGTH"),
	INVALID_URL_EXCEPTION("EXCEPTION.INVALID_URL"),
	INVALID_AGE_EXCEPTION("EXCEPTION.INVALID_AGE"),
	FILE_UPLOAD_FAILED_EXCEPTION("EXCEPTION.FILE_UPLOAD.FAILED"),
	ACTION_NOT_ALLOWED("ACTION_NOT_ALLOWED"),
	ACTION_NOT_SUPPORTED("ACTION_NOT_SUPPORTED"),

	EMAIL_ALREADY_USES("EXCEPTION.EMAIL_ALREADY_USES"),

	CONFIRMATION_TOKEN_INVALID("EXCEPTION.CONFIRMATION.TOKEN.INVALID"),
	CONFIRMATION_TOKEN_WAS_EXPIRED("EXCEPTION.CONFIRMATION.TOKEN.EXPIRED"),

	DATE_PATTERN_PARSE_ERROR("EXCEPTION.DATE_PATTERN_PARSE_ERROR"),

	WRONG_PAGE_SIZE_VALUE("EXCEPTION.WRONG_PAGE_SIZE_VALUE"),
	WRONG_PAGE_INDEX_VALUE("EXCEPTION.WRONG_PAGE_INDEX_VALUE");
	private final String code;

	LocalizedExceptionCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}
}
