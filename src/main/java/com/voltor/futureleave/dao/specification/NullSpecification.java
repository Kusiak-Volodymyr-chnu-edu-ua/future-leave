package com.voltor.futureleave.dao.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.model.Identifiable;

public class NullSpecification<T extends Identifiable> implements Specification<T> {

	private static final long serialVersionUID = 8379712258475972114L;

	private final String fieldPath;

	public NullSpecification(String fieldPath) {
		this.fieldPath = fieldPath;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Path<T> path = SpecificationUtil.buildPath(root, fieldPath);
		return cb.isNull(path);
	}

}
