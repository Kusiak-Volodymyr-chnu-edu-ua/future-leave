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

import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	private UserService userService;
	
	public UserDetailsServiceImpl( UserService userService ) {
		this.userService = userService;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String login ) throws UsernameNotFoundException {
		final User authData = userService.findByLogin( login );
		if( authData==null ) {
			throw new UsernameNotFoundException("User for email address not found");
		}

		return authenticateUser(authData);
	}
	 
	
	public UserDetails authenticateUser(User authData) {
		Role role = Role.SESSION_USER;
		List<GrantedAuthority> authorityList = new ArrayList<>(); 
		authorityList.add(new SimpleGrantedAuthority("ROLE_" + role) );
		return new AuthenticatedUser(authData, role, true, true, true, true, authorityList);
	}
}
