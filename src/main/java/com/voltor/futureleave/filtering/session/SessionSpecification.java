package com.voltor.futureleave.filtering.session;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.Specification;

public class SessionSpecification<EntityType> implements Specification<EntityType> {

	private static final long serialVersionUID = -8146867805381784111L;

	private final Long sessionId;

	public SessionSpecification(Long sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return cb.equal(root.get("sessionId"), this.sessionId);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof SessionSpecification)) return false;

		SessionSpecification< ? > that = (SessionSpecification< ? >) o;

		return new EqualsBuilder()
				.append(sessionId, that.sessionId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(sessionId)
				.toHashCode();
	}
}