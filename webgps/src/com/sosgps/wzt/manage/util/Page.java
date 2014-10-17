package com.sosgps.wzt.manage.util;

import java.util.List;

/**
 * @Title:��װ��ҳ�������ѯ�Ľ��,���̳�QueryParameter�����в�ѯ�������.
 * @Description:
 * @param <T>
 *            Page�еļ�¼����.
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-4 ����03:59:46
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
	 * * ȡ�õ�ת��������
	 */
	public String getInverseOrder() {
		if (order.endsWith(DESC))
			return ASC;
		else
			return DESC;
	}

	/**
	 * * ҳ�ڵ������б�.
	 */
	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * * �ܼ�¼��.
	 */
	public int getTotalCount() {
		return totalCount;
	}
	/**
	 * �����ܼ�¼��ͬʱ������ҳ��
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
	 * * �����ҳ��.
	 */
	public Integer getTotalPages() {
		return totalPages;
	}

	/**
	 * * �Ƿ�����һҳ.
	 */
	public boolean isHasNext() {
		hasNext = (pageNo + 1 <= getTotalPages());
		return hasNext;
	}

	/**
	 * * ������ҳ��ҳ��,��Ŵ�1��ʼ.
	 */
	public int getNextPage() {
		if (isHasNext())
			nextPage = pageNo + 1;
		else
			nextPage = pageNo;
		return nextPage;
	}

	/**
	 * * �Ƿ�����һҳ.
	 */
	public boolean isHasPre() {
		hasPre = (pageNo - 1 >= 1);
		return hasPre;
	}

	/**
	 * * ������ҳ��ҳ��,��Ŵ�1��ʼ.
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
