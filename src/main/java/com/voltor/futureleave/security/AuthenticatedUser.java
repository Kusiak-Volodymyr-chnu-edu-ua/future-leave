package com.voltor.futureleave.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.Role;

public class AuthenticatedUser extends User {

	private static final long serialVersionUID = 6700782328491796936L;

	private final Long sessionId;
	private final String clientId;
	private final Role role;

	public AuthenticatedUser(AuthData authData, Role role, boolean enabled, boolean accountNonExpired,
							 boolean credentialsNonExpired, boolean accountNonLocked,
							 List<GrantedAuthority> authorityList) {
		super( authData.getClientId(), authData.getClientSecretKey(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorityList);
		this.sessionId = authData.getSessionId();
		this.clientId = authData.getClientId();
		this.role = role;
	}
	
	public Long getSessionId() {
		return sessionId;
	}

	public String getClientId() {
		return clientId;
	}

	public Role getRole() {
		return role;
	}

}
