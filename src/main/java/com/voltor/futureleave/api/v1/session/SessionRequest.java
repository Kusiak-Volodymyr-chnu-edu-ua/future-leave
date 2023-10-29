package com.voltor.futureleave.api.v1.session;

import com.voltor.futureleave.api.v1.common.AbstractRequest;
import com.voltor.futureleave.model.Session;

public class SessionRequest extends AbstractRequest<Session> {

	private String name;
	
	@Override
	public Session createEntity() {
		return new Session();
	}

	@Override
	public Session updateEntity(Session entity) {
		entity.setName(name);
		return entity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

