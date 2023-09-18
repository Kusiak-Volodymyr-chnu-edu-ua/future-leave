package com.voltor.futureleave.dao;

import org.springframework.stereotype.Service;

import com.voltor.futureleave.jpa.PrimaryRepository;
import com.voltor.futureleave.jpa.VehicleRepository;
import com.voltor.futureleave.model.Vehicle;
import com.voltor.futureleave.service.AuthenticatedUserService;

@Service
public class VehicleDao extends AbstractIdentifiableDao< Vehicle > {

	private VehicleRepository repository;

	public VehicleDao( AuthenticatedUserService userAuthorizationService, VehicleRepository repository ) {
		super( userAuthorizationService );
		this.repository = repository;
	}

	@Override
	protected PrimaryRepository< Long, Vehicle > getRepository() {
		return repository;
	}
}
