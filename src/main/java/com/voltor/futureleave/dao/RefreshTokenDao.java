package com.voltor.futureleave.dao;


import org.springframework.stereotype.Service;

import com.voltor.futureleave.jpa.PrimaryRepository;
import com.voltor.futureleave.jpa.RefreshTokenRepository;
import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.service.AuthenticatedUserService;

@Service
public class RefreshTokenDao extends AbstractIdentifiableDao<RefreshToken> {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenDao(AuthenticatedUserService userAuthorizationService,
						   RefreshTokenRepository refreshTokenRepository) {
		super(userAuthorizationService);
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	public boolean isEditAllowed(RefreshToken item) {
		return true;
	}
	
	@Override
	protected PrimaryRepository<Long, RefreshToken> getRepository() {
		return refreshTokenRepository;
	}
	
	@Override
	protected boolean entityIsExtendedByUser() {
		return false;
	}


}
