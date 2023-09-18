package com.voltor.futureleave.builder;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voltor.futureleave.model.Session;
import com.voltor.futureleave.service.SessionService;

@Component
public class SessionBuilder {
	
	@Autowired protected SessionService sessionService;
	
	private Session session = new Session();

	public SessionBuilder() {
		initDefaultData();
	}
	
	// Spring based
	public Session toDB() {
		Session session = buildNew();
		return sessionService.create( session );
	}

	// Spring based
	public SessionBuilder initDefaultDBRelations() { 
		
		return this;
	}

	public Session build() {
		Session session = this.session;
		initDefaultData();
		return session;
	}
	
	public Session buildNew() {
		Session session = build();
		session.setId( null );
		return session;
	}

	private void initDefaultData() {
		Long randomValue = ThreadLocalRandom.current().nextLong(1, 999999);
		session = new Session();
		setId( randomValue );
		setName( "someName-" + randomValue );
	}
	
	public static SessionBuilder start() {
    	return new SessionBuilder();
	}
	
	public SessionBuilder setId(Long id) {
		session.setId( id );
		return this;
	}

	public SessionBuilder setName( String name ) {
		session.setName( name );
		return this;
	}


}
