package com.voltor.futureleave.filtering.predicate;

import java.util.Arrays;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.voltor.futureleave.dao.specification.SpecificationUtil;
import com.voltor.futureleave.filtering.FilteringOperation;
import com.voltor.futureleave.filtering.searchcriteria.SearchCriteria;
import com.voltor.futureleave.model.Identifiable;

public class InIdsSpecification< EntityType extends Identifiable > implements Specification< EntityType > {

	private static final long serialVersionUID = -2185458578685293412L;

	private final SearchCriteria searchCriteria;

	public InIdsSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (searchCriteria.getValue() == null) {
			return null;
		}

		if (FilteringOperation.IN != searchCriteria.getOperation() && FilteringOperation.NOT_IN != searchCriteria.getOperation()) {
			return null;
		}


		Path<?> path;
		if (searchCriteria.getKey().equals("id")) {
			path = root.get("id");
		} else {
			path = SpecificationUtil.buildPath(root, searchCriteria.getKey());
		}

		String[] parts = ((String) searchCriteria.getValue()).split("_");
		// If we have a single value for IN operator, it would perform faster if we convert it to EQUAL
		boolean include = searchCriteria.getOperation() == FilteringOperation.IN;

		if (parts.length == 1 && !include) {
			return cb.equal(path, Long.valueOf(parts[0])).not();
		} else if (parts.length == 1 && include) {
			return cb.equal(path, Long.valueOf(parts[0]));
		}

		if (!include) {
			return path.in(Arrays.stream(parts).map(Long::valueOf).toArray()).not();
		}

		return path.in(Arrays.stream(parts).map(Long::valueOf).toArray());
	}

}
