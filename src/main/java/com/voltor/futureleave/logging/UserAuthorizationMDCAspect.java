package com.voltor.futureleave.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voltor.futureleave.service.AuthenticatedUserService;
import com.voltor.futureleave.service.exception.NoCurrentUserException;

@Aspect
@Component
public class UserAuthorizationMDCAspect {
	
	private static final String SESSION_ID_KEY = "SESSION_ID";

	private final AuthenticatedUserService userAuthorizationService;

	@Autowired
	public UserAuthorizationMDCAspect(AuthenticatedUserService userAuthorizationService) {
		this.userAuthorizationService = userAuthorizationService;
	}

	@Around("execution(* com.voltor.futureleave.api.*.*.*(..))")
	public Object aroundSampleCreation( ProceedingJoinPoint proceedingJoinPoint ) throws Throwable {
		try {
			MDC.put( SESSION_ID_KEY, userAuthorizationService.getSessionId() + "" );
		} catch ( NoCurrentUserException ignored ) {
			// Ignore this!
		}

		Object result = proceedingJoinPoint.proceed();
		MDC.remove( SESSION_ID_KEY );

		return result;
	}
}

