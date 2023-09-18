package com.voltor.futureleave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.VehicleDao;
import com.voltor.futureleave.model.Vehicle;

@Service
public class VehicleService extends AbstractService< Vehicle > {
	
	@Autowired private VehicleDao dao;

	@Override
	protected AbstractIdentifiableDao< Vehicle > getDao() {
		return dao;
	}
	
}
