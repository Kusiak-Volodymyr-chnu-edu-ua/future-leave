package com.voltor.futureleave.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.service.AuthDataService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	private AuthDataService authDataService;
	
	public UserDetailsServiceImpl( AuthDataService authDataService ) {
		this.authDataService = authDataService;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String clientId ) throws UsernameNotFoundException {
		final AuthData authData = authDataService.findByClientId( clientId );
		if( authData==null ) {
			throw new UsernameNotFoundException("User for email address not found");
		}

		return authenticateUser(authData);
	}
	
	public UserDetails loadSessionUserBySessionId(final Long sessionId ) throws UsernameNotFoundException {
		final AuthData authData = authDataService.findSessionId( sessionId );
		if( authData==null ) {
			throw new UsernameNotFoundException("User for email address not found");
		}

		return authenticateUser(authData);
	}

	public UserDetails authenticateUser(AuthData authData) {
		Role role = Role.SESSION_USER;
		List<GrantedAuthority> authorityList = new ArrayList<>(); 
		authorityList.add(new SimpleGrantedAuthority("ROLE_" + role) );
		return new AuthenticatedUser(authData, role, true, true, true, true, authorityList);
	}
}
