package com.voltor.futureleave.filtering.user;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecification<EntityType> implements Specification<EntityType> {

	private static final long serialVersionUID = -8146867805381784111L;

	private final Long userId;

	public UserSpecification(Long userId) {
		this.userId = userId;
	}

	@Override
	public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		return cb.equal(root.get("user").get("id"), this.userId);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof UserSpecification)) return false;

		UserSpecification< ? > that = (UserSpecification< ? >) o;

		return new EqualsBuilder()
				.append(userId, that.userId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(userId)
				.toHashCode();
	}
}