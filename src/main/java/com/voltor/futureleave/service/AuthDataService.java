package com.voltor.futureleave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.AuthDataDao;
import com.voltor.futureleave.dao.specification.EqualSpecification;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.service.exception.ActionNotSupportedException;

@Service
public class AuthDataService extends AbstractService< AuthData > {

	@Autowired private AuthDataDao dao;
	
	@Override
	protected AbstractIdentifiableDao< AuthData > getDao() {
		return dao;
	}
	
	@Override
	public AuthData create( AuthData entity ) {
		throw new ActionNotSupportedException();
	}

	@Override
	public AuthData update( AuthData entity, boolean ignorePermissions ) {
		throw new ActionNotSupportedException();
	}

	@Override
	public void delete( long id, boolean ignorePermissions ) {
		throw new ActionNotSupportedException();
	}

	@Override
	public void delete( Specification< AuthData > specification, boolean ignorePermissions ) {
		throw new ActionNotSupportedException();
	}

	public AuthData findByClientId( String clientId ) {
		Specification< AuthData > specification = new EqualSpecification<>( "clientId", clientId ); 
		return dao.getOne( specification );
	}
	
	public AuthData findSessionId( Long sessionId ) {
		Specification< AuthData > specification = new EqualSpecification<>( "sessionId", sessionId ); 
		return dao.getOne( specification );
	}
	
}
