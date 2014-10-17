package org.sos.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.sos.taglibs.pagination.PageIterator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @Title: JdbcSqlExecutor.java
 * @Description:
 * @Copyright:
 * @Date: 2009-4-17 上午11:38:45
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public class JdbcSqlExecutor extends JdbcDaoSupport {
	private static final Logger logger = Logger.getLogger(JdbcSqlExecutor.class);

	protected void initDao() {
		// do nothing
	}

	private String sqlString;

	private Object[] types;// 参数类型数组

	private Object[] params;// 参数对象数组

	private int currentPage;// 当前页

	private int pageSize;// 每页数据条数

	private int totalRowNum;// 总条数

	private int totalPages;// 总页数

	private int startIndex;

	private int lastIndex;

	/**
	 * @see 查询分页数据
	 * @return - List
	 */
	public PageIterator getPaginationList() {
		PageIterator pageIterator = null;
		this.setStartIndex();// 计算起始行数
		this.setLastIndex();// 计算结束行数
		this.setTotalPages();// 计算总页数
		int totalRowNum = this.getTotalRowNum();
		List list = this.getPagination();
		if(logger.isDebugEnabled()) {
			logger.debug("Pagnation Data List is===: " + list);
		}
		pageIterator = new PageIterator(list, currentPage, totalRowNum, pageSize, startIndex, lastIndex,
				totalPages);
		return pageIterator;

	}

	private List getPagination() {
		String sql = this.buildPaginationSql();
		return this.getJdbcTemplate().queryForList(sql);
	}

	/**
	 * @see 查询数据总行数
	 * @return int
	 * @throws Exception
	 */
	private int getTotalRowNum() {
		return this.getJdbcTemplate().queryForInt(this.buildTotalPageSql());
	}

	// 计算起始行数
	public void setStartIndex() {
		this.startIndex = (currentPage - 1) * pageSize;
	}

	// 计算结束时候的索引
	public void setLastIndex() {
		if (totalRowNum < pageSize) {
			this.lastIndex = totalRowNum;
		} else if ((totalRowNum % pageSize == 0) || (totalRowNum % pageSize != 0 && currentPage < totalPages)) {
			this.lastIndex = currentPage * pageSize;
		} else if (totalRowNum % pageSize != 0 && currentPage == totalPages) {// 最后一页
			this.lastIndex = totalRowNum;
		}
	}

	// 计算总页数
	public void setTotalPages() {
		if (totalRowNum % pageSize == 0) {
			this.totalPages = totalRowNum / pageSize;
		} else {
			this.totalPages = (totalRowNum / pageSize) + 1;
		}
	}

	/**
	 * @see build total page sql method
	 * @return String
	 */
	private String buildTotalPageSql() {
		String sql = "select max(rownum) as maxNum from (" + sqlString + ")";
		return sql;
	}

	/**
	 * @see 构建Oracle分页SQL语句
	 * @return
	 */
	private String buildPaginationSql() {
		int i = (currentPage - 1) * pageSize + 1;
		int j = currentPage * pageSize + 1;
		String s = "select * from ( select row_.*, rownum rownum_ from (" + sqlString
				+ ") row_ where rownum < " + j + ") where rownum_ >= " + i;
		if(logger.isDebugEnabled()) {
			logger.debug("Pagination Sql:" + s);
		}
		return s;
	}

	/**
	 * @return the sqlString
	 */
	public String getSqlString() {
		return sqlString;
	}

	/**
	 * @param sqlString
	 *            the sqlString to set
	 */
	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
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
	 * @param lastIndex
	 *            the lastIndex to set
	 */
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	/**
	 * @param totalRowNum
	 *            the totalRowNum to set
	 */
	public void setTotalRowNum(int totalRowNum) {
		this.totalRowNum = totalRowNum;
	}

}
