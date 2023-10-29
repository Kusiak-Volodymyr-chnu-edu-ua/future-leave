package com.voltor.futureleave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.PeriodDao;
import com.voltor.futureleave.model.Period;

@Service
public class PeriodService extends AbstractUserTenencyService<Period> {

	private final PeriodDao periodDao;

	@Autowired
	public PeriodService(PeriodDao periodDao) {
		this.periodDao = periodDao;
	}

	@Override
	protected AbstractIdentifiableDao<Period> getDao() {
		return periodDao;
	}

}
