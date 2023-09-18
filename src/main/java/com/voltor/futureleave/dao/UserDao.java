package com.voltor.futureleave.dao;

import org.springframework.stereotype.Service;

import com.voltor.futureleave.jpa.PrimaryRepository;
import com.voltor.futureleave.jpa.UserRepository;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.AuthenticatedUserService;

@Service
public class UserDao extends AbstractIdentifiableDao< User > {

	private UserRepository repository;

	public UserDao( AuthenticatedUserService userAuthorizationService, UserRepository repository ) {
		super( userAuthorizationService );
		this.repository = repository;
	}

	@Override
	protected PrimaryRepository< Long, User > getRepository() {
		return repository;
	}

}
