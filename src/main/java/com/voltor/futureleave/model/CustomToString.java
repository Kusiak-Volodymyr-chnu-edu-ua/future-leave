package com.voltor.futureleave.model;

import org.apache.commons.lang3.builder.ToStringStyle;

public class CustomToString extends ToStringStyle {
  
	private static final long serialVersionUID = 4447419889961907063L;

	public CustomToString() {
		super();
		this.setContentStart("[");
		this.setFieldSeparator(System.lineSeparator() + "  ");
		this.setFieldSeparatorAtStart(true);
		this.setContentEnd(System.lineSeparator() + "]");
	}

	@Override
	protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
		if (value instanceof Identifiable) {
			value = ((Identifiable) value).getId();
		}
		buffer.append(value);
	}
}
