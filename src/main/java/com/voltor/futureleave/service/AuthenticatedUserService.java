package com.voltor.futureleave.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.service.exception.NoCurrentUserException;

@Service
public class AuthenticatedUserService {

	public boolean isRoot() {
		return Role.ROOT.equals( currentUser().getRole() );
	}

	public boolean isSupport() {
		return Role.SESSION_USER.equals( currentUser().getRole() );
	}
	
	public Long getSessionId() {
		return currentUser().getSessionId();
	}

	private AuthenticatedUser currentUser() throws NoCurrentUserException {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			throw new NoCurrentUserException();
		}

		if (!(authentication.getPrincipal() instanceof AuthenticatedUser)) {
			throw new NoCurrentUserException();
		}

		final AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
		if (authenticatedUser == null ) {
			throw new NoCurrentUserException();
		}

		return authenticatedUser;
	}
	
	public void authenticateUser(UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername());

		authenticateUser(authentication);
	}

	public void authenticateUser(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
