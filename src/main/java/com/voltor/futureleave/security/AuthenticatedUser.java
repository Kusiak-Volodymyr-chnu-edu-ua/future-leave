package com.voltor.futureleave.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.voltor.futureleave.model.Role;

public class AuthenticatedUser extends User {

	private static final long serialVersionUID = 6700782328491796936L;

	private final com.voltor.futureleave.model.User user;
	private final Role role;

	public AuthenticatedUser( com.voltor.futureleave.model.User user, Role role, boolean enabled, boolean accountNonExpired,
							 boolean credentialsNonExpired, boolean accountNonLocked,
							 List<GrantedAuthority> authorityList) {
		super( user.getLogin(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorityList);
		this.user = user;
		this.role = role;
	}
	 
	public com.voltor.futureleave.model.User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}

}
