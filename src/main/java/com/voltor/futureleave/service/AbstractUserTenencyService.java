package com.voltor.futureleave.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.voltor.futureleave.model.UserTenencyEntity;

public abstract class AbstractUserTenencyService<T extends UserTenencyEntity> extends AbstractService<T> {

	@Autowired
	private AuthenticatedUserService userAuthorizationService;

	@Autowired
	private UserService userService;
	
	@Override
	protected void beforeCreate( T entity ) {
		entity.setUser( userService.getOne( userAuthorizationService.getCurrentUserId() ) );
		super.beforeCreate( entity );
	}
	
}
