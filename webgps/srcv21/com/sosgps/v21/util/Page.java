package com.sosgps.v21.util;

import java.util.List;

/**
 * @Title:封装分页和排序查询的结果,并继承QueryParameter的所有查询请求参数.
 * @Description:
 * @param <T>
 *            Page中的记录类型.
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-4 下午03:59:46
 */
public class Page<T> extends QueryParameter {
	private List<T> result = null;

	private int totalCount = 0;

	private int totalPages = 0;

	private boolean hasPre = false;

	private int prePage = 0;

	private boolean hasNext = false;

	private int nextPage = 0;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(int pageSize, boolean autoCount) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
	}

	/**
	 * * 取得倒转的排序方向
	 */
	public String getInverseOrder() {
		if (order.endsWith(DESC))
			return ASC;
		else
			return DESC;
	}

	/**
	 * * 页内的数据列表.
	 */
	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * * 总记录数.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数同时计算总页数
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		if (totalCount == 0) {
			this.totalPages = 0;
		}

		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		this.totalPages = count;
	}

	/**
	 * * 获得总页数.
	 */
	public Integer getTotalPages() {
		return totalPages;
	}

	/**
	 * * 是否还有下一页.
	 */
	public boolean isHasNext() {
		hasNext = (pageNo + 1 <= getTotalPages());
		return hasNext;
	}

	/**
	 * * 返回下页的页号,序号从1开始.
	 */
	public int getNextPage() {
		if (isHasNext())
			nextPage = pageNo + 1;
		else
			nextPage = pageNo;
		return nextPage;
	}

	/**
	 * * 是否还有上一页.
	 */
	public boolean isHasPre() {
		hasPre = (pageNo - 1 >= 1);
		return hasPre;
	}

	/**
	 * * 返回上页的页号,序号从1开始.
	 */
	public int getPrePage() {
		if (isHasPre())
			prePage = pageNo - 1;
		else
			prePage = pageNo;
		return prePage;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public void setHasPre(boolean hasPre) {
		this.hasPre = hasPre;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
}
