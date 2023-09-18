package com.voltor.futureleave.filtering.predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EqualStringSpecification<T> implements Specification<T> {

	private static final long serialVersionUID = -3614399089131893893L;
	
	private final String field;
	private final String value;
	private final boolean ignoreCase;

	public EqualStringSpecification(String field, String value, boolean ignoreCase) {
		this.field = field;
		this.value = value;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		final Path<String> path = root.get(field);
		if (value == null) {
			return cb.isNull(path);
		}
		if (ignoreCase) {
			return cb.equal(cb.lower(path), value.toLowerCase());
		}
		return cb.equal(path, value);
	}

}
