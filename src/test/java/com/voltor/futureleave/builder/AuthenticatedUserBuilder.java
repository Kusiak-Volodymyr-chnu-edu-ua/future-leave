package com.voltor.futureleave.builder;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.voltor.futureleave.model.Role;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.security.AuthenticatedUser;
import com.voltor.futureleave.service.AuthenticatedUserService;

public class AuthenticatedUserBuilder {

	private User user; 
	private Role role;

	public AuthenticatedUserBuilder() {
		initDefaultData();
	}
	
	public AuthenticatedUser build() {
    	User user = this.user;
    	Role role = this.role;
    	user.setUserRole(role);
		List<GrantedAuthority> authorityList = List.of( new SimpleGrantedAuthority("ROLE_" + role) );
		AuthenticatedUser authenticatedUser = new AuthenticatedUser( user, role, true, true, true, true, authorityList );
		initDefaultData();
		return authenticatedUser;
	}
	
	public AuthenticatedUser mock( AuthenticatedUserService userAuthorizationService ) {
		AuthenticatedUser authUser = build();
		given( userAuthorizationService.isRoot() ).willReturn( Role.ROOT.equals( role ) );
		given( userAuthorizationService.isSupport() ).willReturn( Role.SESSION_USER.equals( role ) );
		given( userAuthorizationService.getCurrentUser() ).willReturn( user );
		given( userAuthorizationService.getCurrentUserId() ).willReturn( user.getId() );
		return authUser;
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
		this.user = UserBuilder.start().build();
		this.role = Role.ROOT;
	}
	 
	public AuthenticatedUserBuilder role( Role role ) {
		this.role = role;
		return this;		
	}
	 
	public AuthenticatedUserBuilder user( User user ) {
		this.user = user;
		return this;		
	}
	
}
