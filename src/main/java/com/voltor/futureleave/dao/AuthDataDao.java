package com.voltor.futureleave.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.specification.SpecificationUtil;
import com.voltor.futureleave.jpa.AuthDataRepository;
import com.voltor.futureleave.jpa.PrimaryRepository;
import com.voltor.futureleave.model.AuthData;
import com.voltor.futureleave.service.AuthenticatedUserService;

@Service
public class AuthDataDao extends AbstractIdentifiableDao< AuthData > {

	private AuthDataRepository repository;

	public AuthDataDao(AuthenticatedUserService userAuthorizationService, AuthDataRepository repository) {
		super(userAuthorizationService);
		this.repository = repository;
	}
	
	@Override
	protected PrimaryRepository< Long, AuthData > getRepository() {
		return repository;
	}
	
	@Override
	protected Specification< AuthData > addSpecifications( boolean includeArchivedEntities ) {
		return SpecificationUtil.initEmptySpec();
	}
}
