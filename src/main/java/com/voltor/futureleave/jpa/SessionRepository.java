package com.voltor.futureleave.jpa;

import java.util.Optional;

import com.voltor.futureleave.model.Session;

public interface SessionRepository extends BaseCRUDRepository<Session> {

	Optional<Session> findFirstByName(String name);
}
