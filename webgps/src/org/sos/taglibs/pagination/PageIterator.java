package org.sos.taglibs.pagination;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: PageIterator.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-14 ÏÂÎç02:33:20
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */

public class PageIterator implements Serializable {

	private List collection;

	private int currentPage;

	private int totalRowNum;

	private int pageSize;

	private int startIndex;

	private int lastIndex;

	private int totalPage;

	public PageIterator(List collection1, int currentPage1, int totalRowNum1, int pageSize1, int startIndex1,
			int lastIndex1, int totalPage1) {
		collection = collection1;
		currentPage = currentPage1;
		totalRowNum = totalRowNum1;
		pageSize = pageSize1;
		startIndex = startIndex1;
		lastIndex = lastIndex1;
		totalPage = totalPage1;
	}

	/**
	 * @return the collection
	 */
	public List getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(List collection) {
		this.collection = collection;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the totalRowNum
	 */
	public int getTotalRowNum() {
		return totalRowNum;
	}

	/**
	 * @param totalRowNum the totalRowNum to set
	 */
	public void setTotalRowNum(int totalRowNum) {
		this.totalRowNum = totalRowNum;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the lastIndex
	 */
	public int getLastIndex() {
		return lastIndex;
	}

	/**
	 * @param lastIndex the lastIndex to set
	 */
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	/**
	 * @return the totalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage the totalPage to set
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + currentPage;
		result = prime * result + lastIndex;
		result = prime * result + pageSize;
		result = prime * result + startIndex;
		result = prime * result + totalPage;
		result = prime * result + totalRowNum;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PageIterator other = (PageIterator) obj;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (currentPage != other.currentPage)
			return false;
		if (lastIndex != other.lastIndex)
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (startIndex != other.startIndex)
			return false;
		if (totalPage != other.totalPage)
			return false;
		if (totalRowNum != other.totalRowNum)
			return false;
		return true;
	}

	public String toString(){
	    final String TAB = ",";
	    String retValue = "org.sos.taglibs.pagination.PageIterator=:{"
	        + "collection=" + this.collection + TAB
	        + "currentPage=" + this.currentPage + TAB
	        + "totalRowNum=" + this.totalRowNum + TAB
	        + "pageSize=" + this.pageSize + TAB
	        + "startIndex=" + this.startIndex + TAB
	        + "lastIndex=" + this.lastIndex + TAB
	        + "totalPage=" + this.totalPage
	        + "}";
	    return retValue;
	}
    
	
}
