package com.voltor.futureleave.api.v1.common;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public abstract class AbstractResponse {

	@Schema( 
			description = "The id of the record<br/>"
					+ "Sortable",
			example = "228322" )
	private Long id;

	public AbstractResponse() {
	}

	public AbstractResponse( Long id ) {
		this.id = id;
	}

	public void setId( final Long id ) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash( id );
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		AbstractResponse other = (AbstractResponse) obj;
		return Objects.equals( id, other.id );
	}

	
}
