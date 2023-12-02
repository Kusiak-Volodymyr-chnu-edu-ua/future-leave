package com.voltor.futureleave.api.v1.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.voltor.futureleave.api.v1.ApiConstants;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.security.UserDetailsServiceImpl;
import com.voltor.futureleave.security.jwt.JwtService;
import com.voltor.futureleave.security.jwt.exception.JwtExpirationException;
import com.voltor.futureleave.service.AuthenticatedUserService;
import com.voltor.futureleave.service.RefreshTokenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;

@Controller
@RequestMapping( AuthenticationController.API_URL )
@Tag( name = "Authentication" )
public class AuthenticationController {

	public static final String API_URL = ApiConstants.API_PREFIX;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	private final AuthenticatedUserService userAuthorizationService;

	private final UserDetailsServiceImpl userDetailsService;

	private final RefreshTokenService refreshTokenService;

	@Autowired
	public AuthenticationController(
			AuthenticationManager authenticationManager,
			JwtService jwtService,
			AuthenticatedUserService userAuthorizationService,
			UserDetailsServiceImpl userDetailsService,
			RefreshTokenService refreshTokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userAuthorizationService = userAuthorizationService;
		this.userDetailsService = userDetailsService;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping(path = "/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AuthenticationResponse authenticationRequest(@RequestBody AuthenticationRequest request,
														HttpServletResponse httpServletResponse) {
		String clientId = request.getClientId();
		String clientSecret = request.getClientSecret();

		Authentication authentication = this.authenticationManager
				.authenticate( new UsernamePasswordAuthenticationToken( clientId, clientSecret ) );

		userAuthorizationService.authenticateUser( authentication );
		return buildAuthenticationResponse();
	}

	@PostMapping(path = "/logout")
	@ResponseBody
	public void logout(@RequestParam @NotEmpty String refreshToken) {
		refreshTokenService.delete(refreshToken);
	}

	@PostMapping(path = "/refresh")
	@ResponseBody
	public AuthenticationResponse refreshToken(@RequestParam @NotEmpty String refreshToken) {
		validateRefreshToken(refreshToken);
		User user = refreshTokenService.getTokenData(refreshToken);
		userAuthorizationService.authenticateUser( userDetailsService.authenticateUser( user ) );
		return buildAuthenticationResponse();
	}

	private void validateRefreshToken(String refreshToken){
		try {
			jwtService.verifyToken(refreshToken);
		} catch ( JwtExpirationException expiredException ){
			refreshTokenService.delete( refreshToken );
			throw expiredException;
		}
	}

	private AuthenticationResponse buildAuthenticationResponse() {
		return new AuthenticationResponse( 
				jwtService.generateAccessToken(), 
				jwtService.generateRefreshToken(), 
				jwtService.getAccessTokenExpirationInMinutes(), 
				jwtService.getRefreshTokenExpirationInMinutes() );
	}
}
