package com.voltor.futureleave.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="fl_user", uniqueConstraints = {
		@UniqueConstraint( columnNames = "login" ) 
	}
)
public class User implements Identifiable{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @Column
	private String email;
    
    @Column( name="first_name", nullable = false )
    private String firstName;
    
    @Column( name="last_name", nullable = false )
    private String lastName;
    
    @Column(nullable = false)
    private String login;
    
    @Column(nullable = false)
    private String password;
    
	//NOTE: we use this complex name because 'role' is reserved for our sh..t parser!
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role userRole;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId( Long id ) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin( String login ) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole( Role userRole ) {
		this.userRole = userRole;
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof User ) )
			return false;
		User other = (User) obj;
		return Objects.equals( id, other.id );
	}

}
