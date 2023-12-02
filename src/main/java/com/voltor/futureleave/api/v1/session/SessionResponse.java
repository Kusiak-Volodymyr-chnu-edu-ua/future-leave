package com.voltor.futureleave.api.v1.session;

import com.voltor.futureleave.api.v1.common.AbstractResponse;
import com.voltor.futureleave.model.Session;

public class SessionResponse extends AbstractResponse {
	
	private String name;
	
	public SessionResponse(Session entity) {
		super( entity.getId() );
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

