package com.voltor.futureleave.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class UserTenencyEntity implements Identifiable {
 
	@ManyToOne(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
	protected User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
