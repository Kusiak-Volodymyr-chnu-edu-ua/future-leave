package com.voltor.futureleave.api.v1.common;

import java.util.List;

public class PageResponse<T> {

	private long totalElements;
	private long index;
	private long size;
	private List< T > items;

	public PageResponse() {
		//it is needed for JSON parsing
	}

	public PageResponse( List< T > items, long totalElements, long index, long size) {
		this.totalElements = totalElements;
		this.items = items;
		this.index = index;
		this.size = size;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public void setItems(List< T > items) {
		this.items = items;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public List< T > getItems() {
		return items;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
