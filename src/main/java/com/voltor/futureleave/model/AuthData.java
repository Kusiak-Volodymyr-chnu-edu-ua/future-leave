package com.voltor.futureleave.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table(name = "auth_data")
public class AuthData extends SessionTenencyEntity implements Identifiable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column( nullable = true, name = "client_id" )
	private String clientId;
	
	@Column( nullable = true, name = "client_secret_key" )
	private String clientSecretKey;

	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId( Long id ) {
		this.id = id;
	}

	public String getClientId() {
		return this.clientId;
	}
	
	public void setClientId( String clientId ) {
		this.clientId = clientId;
	}

	public String getClientSecretKey() {
		return this.clientSecretKey;
	}
	
	public void setClientSecretKey( String clientSecretKey ) {
		this.clientSecretKey = clientSecretKey;
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		AuthData authData = (AuthData) o;
		return new EqualsBuilder()
				.append( id, authData.id )
				.isEquals();
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}

}