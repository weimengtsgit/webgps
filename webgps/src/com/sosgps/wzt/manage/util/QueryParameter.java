package com.sosgps.wzt.manage.util;

import org.apache.commons.lang.StringUtils;

/**
 * @Title:�����װ��ҳ�������ѯ�������. 
 * @Description:����ο���springside��ORM��װ���
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-4 ����03:58:54
 */
public class QueryParameter {
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	protected int pageNo = 1;
	protected int pageSize = 0;
	protected String orderBy = null;
	protected String order = ASC;
	protected boolean autoCount = false;

	/**
	 * ���ÿҳ�ļ�¼����,��Ĭ��ֵ.
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * �Ƿ�������ÿҳ�ļ�¼����.
	 */
	public boolean isPageSizeSetted() {
		return pageSize > 0;
	}

	/**
	 * ��õ�ǰҳ��ҳ��,��Ŵ�1��ʼ,Ĭ��Ϊ1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * ����pageNo��pageSize���㵱ǰҳ��һ����¼���ܽ�����е�λ��,��Ŵ�0��ʼ.
	 */
	public int getFirst() {
		if (pageNo < 1 || pageSize < 1)
			return 0;
		else
			return ((pageNo - 1) * pageSize);
	}

	/**
	 * �Ƿ������õ�һ����¼��¼���ܽ�����е�λ��.
	 */
	public boolean isFirstSetted() {
		return (pageNo > 0 && pageSize > 0);
	}

	/**
	 * ��������ֶ�,��Ĭ��ֵ.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * �Ƿ������������ֶ�.
	 */
	public boolean isOrderBySetted() {
		return StringUtils.isNotBlank(orderBy);
	}

	/**
	 * ���������,Ĭ��Ϊasc.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * ��������ʽ��.
	 * 
	 * @param order
	 *            ��ѡֵΪdesc��asc.
	 */
	public void setOrder(String order) {
		if (ASC.equalsIgnoreCase(order) || DESC.equalsIgnoreCase(order)) {
			this.order = order.toLowerCase();
		} else
			throw new IllegalArgumentException(
					"order should be 'desc' or 'asc'");
	}

	/**
	 * �Ƿ��Զ���ȡ��ҳ��,Ĭ��Ϊfalse. ע�Ȿ���Խ���query by Criteriaʱ��Ч,query by HQLʱ��������Ч.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}
}
