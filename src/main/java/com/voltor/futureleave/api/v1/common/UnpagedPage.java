package com.voltor.futureleave.api.v1.common;

import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class UnpagedPage implements Pageable, Serializable {

	private static final long serialVersionUID = 1232825578694716871L;

	private final Sort sort;

	public UnpagedPage(Sort sort ) {
		Assert.notNull(sort, "Sort must not be null!");
		this.sort = sort;
	}

	public Sort getSort() {
		return this.sort;
	}

	@Override
	public boolean isPaged() {
		return false;
	}

	public Pageable previousOrFirst() {
		return this;
	}

	public Pageable next() {
		return this;
	}

	public boolean hasPrevious() {
		return false;
	}

	public int getPageSize() {
		throw new UnsupportedOperationException();
	}

	public int getPageNumber() {
		throw new UnsupportedOperationException();
	}

	public long getOffset() {
		throw new UnsupportedOperationException();
	}

	public Pageable first() {
		return this;
	}

	@Override
	public Pageable withPage(int pageNumber)  {
		throw new UnsupportedOperationException();
	}

	public boolean equals(@Nullable Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof UnpagedPage)) {
			return false;
		} else {
			UnpagedPage that = (UnpagedPage)obj;
			return super.equals(that) && this.sort.equals(that.sort);
		}
	}


	public int hashCode() {
		return 31 * super.hashCode() + this.sort.hashCode();
	}

	public String toString() {
		return String.format("Page request [sort: %s]", this.sort);
	}

}
