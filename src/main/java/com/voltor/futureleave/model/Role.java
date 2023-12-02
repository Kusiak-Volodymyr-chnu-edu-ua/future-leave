package com.voltor.futureleave.model;

public enum Role {
	
	ROOT,
	SESSION_USER;
	
	public int getId() {
		return this.ordinal();
	}
	 
}
