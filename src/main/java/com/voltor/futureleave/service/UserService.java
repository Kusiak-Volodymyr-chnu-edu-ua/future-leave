package com.voltor.futureleave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.UserDao;
import com.voltor.futureleave.dao.specification.EqualSpecification;
import com.voltor.futureleave.dao.specification.NotEqualSpecification;
import com.voltor.futureleave.model.User;

@Service
public class UserService extends AbstractService< User > {
	
	@Autowired private UserDao dao;

	@Override
	protected AbstractIdentifiableDao< User > getDao() {
		return dao;
	}
	
	public User createOrUpdate( User user ) {
		if( user.getId() == null ) {
			return create( user );
		}
		return update( user );
	}
	
	@Override
	protected void beforeCreate(User entity) {
		entity.setPassword( EncryptingUtils.crypt( entity.getPassword() ) );
	}
	
	@Override
	protected void beforeUpdate(User entity) {
		User oldEntity = dao.getOneArchived( entity.getId(), true );
		if( !oldEntity.getPassword().equals( entity.getPassword() ) ) {
			entity.setPassword( EncryptingUtils.crypt( entity.getPassword() ) );
		}
	}

	public boolean isLoginUnique( User user ) {
		Specification< User > idFilter = new NotEqualSpecification<>( "id", user.getId() );
		Specification< User > loginFilter = new EqualSpecification<>( "login", user.getLogin() );
		return !getDao().existsIncludingArchived( loginFilter.and( idFilter ), true ); 
	}
	
}
