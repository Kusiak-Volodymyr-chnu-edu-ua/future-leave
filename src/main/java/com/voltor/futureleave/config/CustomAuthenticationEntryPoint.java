package com.voltor.futureleave.config;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.voltor.futureleave.api.v1.ApiConstants;

/**
 * This custom BasicAuthenticationEntryPoint is used to disable Browser's build in
 * Basic authentication dialog. Id does it by overriding 'WWW-Authenticate' response header.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String LOGIN_URL = ApiConstants.API_PREFIX + ApiConstants.AUTHENTICATION_ENDPOINT;
	private static final String LOGIN_URL_V1 = ApiConstants.API_PREFIX + ApiConstants.API_VERSION_V1 + ApiConstants.AUTHENTICATION_ENDPOINT;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		if ((request.getContextPath() + LOGIN_URL).equalsIgnoreCase(request.getRequestURI())
				|| (request.getContextPath() + LOGIN_URL_V1).equalsIgnoreCase(request.getRequestURI())) {
			response.setHeader("WWW-Authenticate", "FormBased");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		} else if (response.getHeader("WWW-Authenticate") == null) {
			response.addHeader("WWW-Authenticate", "Basic realm=\"sample\"");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					authException.getMessage());
		}
	}
}