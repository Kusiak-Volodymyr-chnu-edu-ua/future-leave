package com.voltor.futureleave.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.RefreshTokenDao;
import com.voltor.futureleave.dao.specification.EqualSpecification;
import com.voltor.futureleave.model.RefreshToken;
import com.voltor.futureleave.model.User;
import com.voltor.futureleave.service.exception.RefreshTokenNotFoundException;

@Service
public class RefreshTokenService extends AbstractUserTenencyService<RefreshToken> {

	@Autowired
	private RefreshTokenDao refreshTokenDao;
	
 

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
	 
	public User getTokenData(String refreshToken) {
		RefreshToken token = getOne(new EqualSpecification<>("token", refreshToken));
		if (token == null) {
			throw new RefreshTokenNotFoundException();
		}
		delete( token.getId() );
		return token.getUser();
	}

	public void delete(String refreshToken) {
		delete(new EqualSpecification<>("token", refreshToken), true);
	}
}
