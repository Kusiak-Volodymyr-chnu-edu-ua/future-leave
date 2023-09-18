package com.voltor.futureleave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltor.futureleave.dao.AbstractIdentifiableDao;
import com.voltor.futureleave.dao.SessionDao;
import com.voltor.futureleave.dao.exception.ActionNotAllowedException;
import com.voltor.futureleave.localization.LocalizedExceptionCode;
import com.voltor.futureleave.localization.LocalizedMessage;
import com.voltor.futureleave.model.Session;

@Service
public class SessionService extends AbstractService<Session> {

	private final SessionDao sessionDao;

	@Autowired
	public SessionService(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	@Override
	protected AbstractIdentifiableDao<Session> getDao() {
		return sessionDao;
	}
	
	public Session getCurrentSession() {
		return sessionDao.get().iterator().next();
	}

	@Override
	public void delete(long id, boolean ignorePermissions) {
		String msg = "Removing session is not allowed";
		throw new ActionNotAllowedException(new LocalizedMessage(msg, LocalizedExceptionCode.REMOVAL_NOT_ALLOWED_EXCEPTION_MESSAGE));
	}
}
