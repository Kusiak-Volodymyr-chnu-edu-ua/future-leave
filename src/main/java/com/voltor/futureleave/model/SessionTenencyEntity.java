package com.voltor.futureleave.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SessionTenencyEntity extends UserTenencyEntity {
 
	@Column(name = "session_id")
	protected Long sessionId;

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId( Long sessionId ) {
		this.sessionId = sessionId;
	}

}
