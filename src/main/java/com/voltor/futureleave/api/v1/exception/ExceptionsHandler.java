package com.voltor.futureleave.api.v1.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.voltor.futureleave.dao.exception.ActionNotAllowedException;
import com.voltor.futureleave.dao.exception.RemovalNotAllowedException;
import com.voltor.futureleave.dao.exception.UpdateNotAllowedException;
import com.voltor.futureleave.filtering.IllegalFilteringOperationException;
import com.voltor.futureleave.localization.LocalizedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;
import com.voltor.futureleave.security.jwt.exception.JwtBadSignatureException;
import com.voltor.futureleave.security.jwt.exception.JwtExpirationException;
import com.voltor.futureleave.security.jwt.exception.MalformedJwtException;
import com.voltor.futureleave.service.exception.ConstraintValidationException;
import com.voltor.futureleave.service.exception.DuplicatedValueException;
import com.voltor.futureleave.service.exception.MaxCountValidationException;
import com.voltor.futureleave.service.exception.NoCurrentUserException;
import com.voltor.futureleave.service.exception.ParseDatePatternException;
import com.voltor.futureleave.service.exception.RefreshTokenNotFoundException;
import com.voltor.futureleave.service.exception.RetrievalNotAllowedException;
import com.voltor.futureleave.service.exception.UserAlreadyExistsException;

@ControllerAdvice(basePackages = {
		"com.voltor.futureleave.api"
})
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsHandler.class);

	private static final Map<Class<? extends Exception>, HttpStatus> STATUSES = new HashMap<>();

	/** In order to your class be handled, you should add it to STATUSES **/
	static {
		STATUSES.put(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
		STATUSES.put(UnsupportedActionException.class, HttpStatus.METHOD_NOT_ALLOWED);
		STATUSES.put(UserAlreadyExistsException.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(RetrievalNotAllowedException.class, HttpStatus.FORBIDDEN);
		STATUSES.put(ConstraintValidationException.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(ObjectNotFoundException.class, HttpStatus.NOT_FOUND);
		STATUSES.put(MaxCountValidationException.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(RemovalNotAllowedException.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(InvalidConfirmationToken.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(ConfirmationTokenExpired.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(ActionNotAllowedException.class, HttpStatus.NOT_ACCEPTABLE);
		STATUSES.put(RefreshTokenNotFoundException.class, HttpStatus.UNAUTHORIZED);
		STATUSES.put(UpdateNotAllowedException.class, HttpStatus.METHOD_NOT_ALLOWED);
		STATUSES.put(ParseDatePatternException.class, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = DuplicatedValueException.class)
	protected ResponseEntity<Object> handleDuplicatedValueException(DuplicatedValueException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.BAD_REQUEST);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler(value = {IllegalFilteringOperationException.class})
	protected ResponseEntity<Object> handleIllegalFilteringOperationException(IllegalFilteringOperationException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.BAD_REQUEST);
		return handleExceptionInternal(ex, error, request);
	}
	
	@ExceptionHandler(value = {IOException.class})
	protected ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
		if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(ex), "Broken pipe")) {
			return null;
		} else {
			final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.INTERNAL_SERVER_ERROR);
			return handleExceptionInternal(ex, error, request);
		}
	}

	@ExceptionHandler(value = {MultipartException.class})
	protected ResponseEntity<Object> handleMultipartException(MultipartException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.INTERNAL_SERVER_ERROR);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler(value = {ConstraintViolationException.class})
	protected ResponseEntity<Object> handleValidationFailedException(ConstraintViolationException ex, WebRequest request) {
		final String details = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
		final String defaultMessage = String.format("Validation failed, %s", details);
		final LocalizedMessage localizedMessage = new LocalizedMessage(defaultMessage, LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION);

		final LocalizedApiError error = new LocalizedApiError(localizedMessage, HttpStatus.NOT_ACCEPTABLE, ex.getClass().getSimpleName());

		return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
	}

	@ExceptionHandler(value = {IllegalArgumentException.class})
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.INTERNAL_SERVER_ERROR);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler(value = {IllegalStateException.class})
	protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.INTERNAL_SERVER_ERROR);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler(value = {LocalizedException.class})
	protected ResponseEntity<Object> handleLocalizedException(LocalizedException ex, WebRequest request) {
		final LocalizedApiError error = new LocalizedApiError(ex, STATUSES.getOrDefault(ex.getClass(),
				HttpStatus.INTERNAL_SERVER_ERROR));
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler( value = {BadCredentialsException.class})
	protected void handleBadCredentialsException(BadCredentialsException ex, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@ExceptionHandler( value = {DateTimeParseException.class})
	protected ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex,  WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.UNPROCESSABLE_ENTITY);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler( value = {JwtExpirationException.class, JwtBadSignatureException.class, MalformedJwtException.class})
	protected void handleJWTVerificationException(RuntimeException ex, HttpServletResponse response) throws IOException {
	 	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@ExceptionHandler(value = {FileNotFoundException.class})
	protected void handleFileNotFoundException(FileNotFoundException ex, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(value = {JobExecutionAlreadyRunningException.class})
	protected ResponseEntity<Object> handleJobExecutionAlreadyRunningException(
			JobExecutionAlreadyRunningException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.CONFLICT);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<Object> handleCommonException(Exception ex, WebRequest request) {
		final LocalizedMessage localizedMessage = new LocalizedMessage("Error occurred.", LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION);
		LocalizedApiError error = new LocalizedApiError(localizedMessage,
				HttpStatus.INTERNAL_SERVER_ERROR,
				ex.getClass().getSimpleName());
		return handleExceptionInternal(ex, error, request);
	}
	
	@ExceptionHandler(value = {PatchFieldConstraintViolationException.class})
	protected ResponseEntity<Object> handlePatchFieldConstraintViolationException(PatchFieldConstraintViolationException ex, WebRequest request) {		
		final StringJoiner stringBuilder = new StringJoiner(". ");
		stringBuilder.add("Error occurred");

		for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
			final String defaultMessage = String.format("%s: %s", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
			stringBuilder.add(defaultMessage);
		}
		final LocalizedMessage localizedMessage = new LocalizedMessage(stringBuilder.toString(), LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION);

		final LocalizedApiError error = new LocalizedApiError(localizedMessage, HttpStatus.NOT_ACCEPTABLE, ex.getClass().getSimpleName());

		return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
																  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		LOGGER.error("Failed to process the HTTP request {}", ex.getMessage());
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return handleExceptionInternal(ex, error, request);
	}
	
	

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatusCode status,
																  WebRequest request) {

		final StringJoiner stringBuilder = new StringJoiner(". ");
		stringBuilder.add("Error occurred");

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			final String defaultMessage = String.format("%s: %s", error.getField(), error.getDefaultMessage());
			stringBuilder.add(defaultMessage);
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			final String defaultMessage = String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
			stringBuilder.add(defaultMessage);
		}
		final LocalizedMessage localizedMessage = new LocalizedMessage(stringBuilder.toString(), LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION);

		final LocalizedApiError error = new LocalizedApiError(localizedMessage, status, ex.getClass().getSimpleName());

		return handleExceptionInternal(ex, error, headers, status, request);
	}

	private ResponseEntity<Object> handleExceptionInternal(Exception ex, LocalizedApiError error, WebRequest request) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return handleExceptionInternal(ex, error, headers, error.getStatus(), request);
	}

	private LocalizedApiError buildErrorOccurred(Exception ex, HttpStatus status) {

		final String defaultDescription = String.format("Error occurred. %s", ex.getMessage());
		final LocalizedMessage localizedMessage = new LocalizedMessage(defaultDescription, LocalizedExceptionCode.ERROR_OCCURRED_EXCEPTION);
		return new LocalizedApiError(localizedMessage, status, ex.getClass().getSimpleName());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
															 Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		LOGGER.warn("Exception occurred:", ex);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@ExceptionHandler(value = {NoCurrentUserException.class})
	protected ResponseEntity<Object> handleNoCurrentUserExceptionException(NoCurrentUserException ex, WebRequest request) {
		final LocalizedApiError error = buildErrorOccurred(ex, HttpStatus.UNAUTHORIZED);
		return handleExceptionInternal(ex, error, request);
	}

	@ExceptionHandler(value = {ClientAbortException.class})
	public void handleClientAbortException(ClientAbortException exception, HttpServletRequest request) {
		final String message = "ClientAbortException generated by request {} {} from remote address {} ";
		LOGGER.warn(message, request.getMethod(), request.getRequestURL(), request.getRemoteAddr());
	}

}
