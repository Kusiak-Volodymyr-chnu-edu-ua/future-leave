package com.voltor.futureleave.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.RefreshTokenDao;
import com.voltor.futureleave.dao.specification.EqualSpecification;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.service.exception.RefreshTokenNotFoundException;

@Service
public class RefreshTokenService extends AbstractService<RefreshToken> {

	@Autowired
	private RefreshTokenDao refreshTokenDao;

	@Autowired
	private AuthenticatedUserService userAuthorizationService;
	
	@Autowired
	private AuthDataService authDataService;

	@Override
	protected AbstractIdentifiableDao<RefreshToken> getDao() {
		return refreshTokenDao;
	}

	public RefreshToken create( String token, ZonedDateTime expiringDate ) {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken( token );
		refreshToken.setExpiringDate( expiringDate );
		return super.create( refreshToken );
	}
	
	@Override
	protected void beforeCreate( RefreshToken refreshToken ) {
		refreshToken.setSessionId( userAuthorizationService.getSessionId() );
		super.beforeCreate( refreshToken );
	}

	public AuthData getTokenData(String refreshToken) {
		RefreshToken token = getOne(new EqualSpecification<>("token", refreshToken));
		if (token == null) {
			throw new RefreshTokenNotFoundException();
		}
		delete( token.getId() );
		return authDataService.findSessionId( token.getSessionId() );
	}

	public void delete(String refreshToken) {
		delete(new EqualSpecification<>("token", refreshToken), true);
	}
}
