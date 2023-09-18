package com.voltor.futureleave.builder;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.service.AuthenticatedUserService;

public class AuthenticatedUserBuilder {

	private AuthData authData; 
	private Role role;

	public AuthenticatedUserBuilder() {
		initDefaultData();
	}
	
	public AuthenticatedUser build() {
    	AuthData authData = this.authData;
    	Role role = this.role;
		List<GrantedAuthority> authorityList = List.of( new SimpleGrantedAuthority("ROLE_" + role) );
		AuthenticatedUser authenticatedUser = new AuthenticatedUser( authData, role, true, true, true, true, authorityList );
		initDefaultData();
		return authenticatedUser;
	}
	
	public AuthenticatedUser mock( AuthenticatedUserService userAuthorizationService ) {
		AuthenticatedUser user = build();
		given( userAuthorizationService.isRoot() ).willReturn( Role.ROOT.equals( role ) );
		given( userAuthorizationService.isSupport() ).willReturn( Role.SESSION_USER.equals( role ) );
		given( userAuthorizationService.getSessionId() ).willReturn( user.getSessionId() );
		return user;
	}
	
	public void login() {
		AuthenticatedUser user = build();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( user,
				user.getUsername() );
		SecurityContextHolder.getContext().setAuthentication( authentication );
	}
	
	public static AuthenticatedUserBuilder start() {
    	return new AuthenticatedUserBuilder();
	}

	private void initDefaultData() {
		this.authData = AuthDataBuilder.start().build();
		this.role = Role.ROOT;
	}
	
	public AuthenticatedUserBuilder clientId( String clientId ) {
		authData.setClientId( clientId );
		return this;		
	}
	
	public AuthenticatedUserBuilder role( Role role ) {
		this.role = role;
		return this;		
	}
	
	public AuthenticatedUserBuilder sessionId( Long sessionId ) {
		authData.setSessionId( sessionId );
		return this;		
	}
	
	public AuthenticatedUserBuilder authData( AuthData authData) {
		this.authData = authData;
		return this;		
	}
	
}
