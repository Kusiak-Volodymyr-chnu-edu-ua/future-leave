package com.voltor.futureleave.filtering.searchcriteria;

import java.io.Serializable;

import com.voltor.futureleave.filtering.FilteringOperation;

public class SearchCriteria implements Serializable {
 
	private static final long serialVersionUID = -4654470036504265513L;
	
	private final String key;
	private final FilteringOperation operation;
	private final Object value;

	public SearchCriteria(String key, FilteringOperation operation, Object value) {
		this.key = key;
		this.operation = operation;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public FilteringOperation getOperation() {
		return operation;
	}

	public Object getValue() {
		return value;
	}

	public Class<?> getType() {
		return value == null ? null : value.getClass();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SearchCriteria that = (SearchCriteria) o;

		if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
		if (getOperation() != null ? !getOperation().equals(that.getOperation()) : that.getOperation() != null)
			return false;
		return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;
	}

	@Override
	public int hashCode() {
		int result = getKey() != null ? getKey().hashCode() : 0;
		result = 31 * result + (getOperation() != null ? getOperation().hashCode() : 0);
		result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SearchCriteria[" + "key='" + key + '\'' + ", operation='" + operation + '\'' + ", value=" + value + ']';
	}
}
