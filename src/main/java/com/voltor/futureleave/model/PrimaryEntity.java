package com.voltor.futureleave.model;

public interface PrimaryEntity<T> {
	
	T getId();

	void setId(T id);
}
