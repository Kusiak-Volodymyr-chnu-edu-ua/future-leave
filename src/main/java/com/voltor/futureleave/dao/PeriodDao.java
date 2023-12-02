package com.voltor.futureleave.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.jpa.BaseCRUDRepository;
import com.voltor.futureleave.jpa.PeriodRepository;
import com.voltor.futureleave.model.Period;
import com.voltor.futureleave.service.AuthenticatedUserService;

@Service
public class PeriodDao extends AbstractIdentifiableDao<Period> {

	private final PeriodRepository periodRepository;

	@Autowired
	public PeriodDao(AuthenticatedUserService userAuthorizationService, PeriodRepository periodRepository) {
		super(userAuthorizationService);
		this.periodRepository = periodRepository;
	}

	@Override
	protected BaseCRUDRepository<Period> getRepository() {
		return periodRepository;
	}
 
}
