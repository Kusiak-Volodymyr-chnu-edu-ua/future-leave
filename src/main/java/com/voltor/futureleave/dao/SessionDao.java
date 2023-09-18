package com.voltor.futureleave.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.jpa.BaseCRUDRepository;
import com.voltor.futureleave.jpa.SessionRepository;
import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.service.AuthenticatedUserService;

@Service
public class SessionDao extends AbstractIdentifiableDao<Session> {

	private final SessionRepository sessionRepository;

	@Autowired
	public SessionDao(AuthenticatedUserService userAuthorizationService, SessionRepository sessionRepository) {
		super(userAuthorizationService);
		this.sessionRepository = sessionRepository;
	}

	@Override
	protected BaseCRUDRepository<Session> getRepository() {
		return sessionRepository;
	}


	@Override
	public boolean isEditAllowed( Session entity ) {
		return userAuthorizationService.isRoot();
	}
	

	public Session getDefaultSession() {
		return sessionRepository.findAll().iterator().next();
	}
}
