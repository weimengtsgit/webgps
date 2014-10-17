/**
 * 
 */
package com.sosgps.v21.dao.impl.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @title IHibernateCallback
 * @author ZZQ
 * @version time: 2012-5-17 下午5:32:06
 * @copyright (c) www.sosgps.net.cn
 * @description
 * 
 */
public class CommonHibernateCallback implements HibernateCallback {

	public boolean isQuery = true;

	private String hql;

	private Object[] values;

	private Integer firstResult;

	private Integer maxResults;

	public CommonHibernateCallback(String hql) {
		super();
		this.hql = hql;
	}

	public CommonHibernateCallback(String hql, boolean isQuery) {
		super();
		this.hql = hql;
		this.isQuery = isQuery;
	}

	public CommonHibernateCallback(String hql, Object[] values) {
		super();
		this.hql = hql;
		this.values = values;
	}

	public CommonHibernateCallback(String hql, Object[] values, boolean isQuery) {
		super();
		this.hql = hql;
		this.values = values;
		this.isQuery = isQuery;
	}

	public CommonHibernateCallback(String hql, Integer firstResult,
			Integer maxResults) {
		super();
		this.hql = hql;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public CommonHibernateCallback(String hql, Object[] values,
			Integer firstResult, Integer maxResults) {
		super();
		this.hql = hql;
		this.values = values;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.orm.hibernate3.HibernateCallback#doInHibernate(org
	 *      .hibernate.Session)
	 */
	public Object doInHibernate(Session session) throws HibernateException,
			SQLException {
		if (hql != null && values == null && firstResult == null
				&& maxResults == null) {
			return doInHibernate(session, hql);
		} else if (hql != null && values != null && firstResult == null
				&& maxResults == null) {
			return doInHibernate(session, hql, values);

		} else if (hql != null && values == null && firstResult != null
				&& maxResults != null) {
			return doInHibernate(session, hql, firstResult, maxResults);

		} else if (hql != null && values != null && firstResult != null
				&& maxResults != null) {
			return doInHibernate(session, hql, values, firstResult, maxResults);
		} else {
			throw new SQLException("parameters error.");
		}
	}

	private Object doInHibernate(Session session, String hql)
			throws HibernateException, SQLException {
		if (isQuery) {
			return createQuery(session, hql).list();
		} else {
			return createQuery(session, hql).executeUpdate();
		}
	}

	private Object doInHibernate(Session session, String hql, Object[] values)
			throws HibernateException, SQLException {
		if (isQuery) {
			return createQuery(session, hql, values).list();
		} else {
			return createQuery(session, hql, values).executeUpdate();
		}
	}

	private Object doInHibernate(Session session, String hql,
			Integer firstResult, Integer maxResults) throws HibernateException,
			SQLException {
		return session.createQuery(hql).setFirstResult(firstResult)
				.setMaxResults(maxResults).list();
	}

	private Object doInHibernate(Session session, String hql, Object[] values,
			Integer firstResult, Integer maxResults) throws HibernateException,
			SQLException {
		return createQuery(session, hql, values).setFirstResult(firstResult)
				.setMaxResults(maxResults).list();
	}

	public Query createQuery(Session session, String hql) {
		return session.createQuery(hql);
	}

	public Query createQuery(Session session, String hql, Object... values) {
		Query query = session.createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}
}
