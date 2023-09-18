package com.voltor.futureleave.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "refresh_token")
public class RefreshToken extends SessionTenencyEntity implements Identifiable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "token", nullable = false, unique = true)
	@NotBlank
	private String token;

	@Column(name = "expiring_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
	private ZonedDateTime expiringDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ZonedDateTime getExpiringDate() {
		return expiringDate;
	}

	public void setExpiringDate(ZonedDateTime expiringDate) {
		this.expiringDate = expiringDate;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		RefreshToken other = (RefreshToken) o;
		return Objects.equals( id, other.id );
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}
}
