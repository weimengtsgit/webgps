/**
 * 
 */
package com.sosgps.wzt.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.v21.interceptor.CustomizeInterceptor;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;

/**
 * @author xiaojun.luan
 *
 */
public class PageQuery<T> {
	
	public PageQuery(HibernateDaoSupport hibernateDaoSupport){
		this.hibernateDaoSupport=hibernateDaoSupport;
	}
	/**
	 * 
	 * @param page
	 * @param sql
	 *            SQL语句
	 * @param values
	 *            待填充的参数值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findByPageWithSql(final Page<T> page, final String sql,
			final Object... values) {

		try {
			// 如果设置获取总数，则先获取总数
			hibernateDaoSupport.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							if (page.isAutoCount()) {
								Long li = getDataTotalWithSql(s, sql, values);
								int totalCount = 0;
								if (li != null) {
									totalCount = li.intValue();
								}
								page.setTotalCount(totalCount);// 设置总记录数
								page.getNextPage();// 是否还有下页
								page.getPrePage();// 是否还有上页
							}
							return page;
						}
					});
			return (Page<T>) hibernateDaoSupport.getHibernateTemplate()
					.execute(new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							Query query = createSqlQuery(s, sql, values);
							if (page.isFirstSetted()) {
								query.setFirstResult(page.getFirst());
							}
							if (page.isPageSizeSetted()) {
								query.setMaxResults(page.getPageSize());
							}
							page.setResult(query.list());

							return page;
						}
					});
		} catch (RuntimeException e) {
			 SysLogger.sysLogger.error("分页查询异常，SQL：" + sql, e);
			throw e;
		}

	}
	private HibernateDaoSupport hibernateDaoSupport;
	public Page<T> findByPage(final Page<T> page,
			final String hql, final Object... values) {
		try {
			// 如果设置获取总数，则先获取总数
			hibernateDaoSupport.getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
//					String countHql = hql.substring(hql.indexOf("from"));
//					countHql = "select count(*) " + countHql;
//					Query query = createQuery(s, countHql, values);
//					if (page.isAutoCount()) {
//						List li = query.list();
//						int totalCount = 0;
//						if (li != null) {
//							totalCount = ((Long)li.get(0)).intValue();
//						}
//						page.setTotalCount(totalCount);// 设置总记录数
//						page.getNextPage();// 是否还有下页
//						page.getPrePage();// 是否还有上页
//					}
					if (page.isAutoCount()) {
						Long li = getDataTotal(s, hql, values);
						int totalCount = 0;
						if (li != null) {
							totalCount = li.intValue();
						}
						page.setTotalCount(totalCount);// 设置总记录数
						page.getNextPage();// 是否还有下页
						page.getPrePage();// 是否还有上页
					}
					return page;
				}
			});
			return (Page<T>) hibernateDaoSupport.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							Query query = createQuery(s, hql, values);
							if (page.isFirstSetted()) {
								query.setFirstResult(page.getFirst());
							}
							if (page.isPageSizeSetted()) {
								query.setMaxResults(page.getPageSize());
							}
							page.setResult(query.list());

							return page;
						}
					});
		} catch (RuntimeException e) {
			SysLogger.sysLogger.error("分页查询异常，HQL：" + hql, e);
			throw e;
		}
	}
	/**
	 * 
	 * @param session
	 * @param sql
	 * @param objects
	 * @return
	 */
	private Query createSqlQuery(Session session, String sql, Object... objects) {
		Query query = session.createSQLQuery(sql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}

	private Query createQuery(Session session, String hql, Object... objects) {
		Query query = session.createQuery(hql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}
	/**
	 * 获取hql语句中返回的记录条数，通过转成SQL来进行查询，解决hql不能在distinct，group by结果集上使用count的问题
	 */
	public Long getDataTotal(Session session, String hql, Object[] values) {

		org.hibernate.hql.classic.QueryTranslatorImpl queryTranslator = new org.hibernate.hql.classic.QueryTranslatorImpl(
				hql, hql, Collections.EMPTY_MAP,
				(SessionFactoryImplementor) hibernateDaoSupport
						.getSessionFactory());
		queryTranslator.compile(Collections.EMPTY_MAP, false);
		String tempSQL = queryTranslator.getSQLString();
		String countSQL = "select count(*) from (" + tempSQL + ") tmp_count_t";
		Query query = session.createSQLQuery(countSQL);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		List list = query.list();
		BigDecimal count = list == null || list.size() <= 0 ? new BigDecimal(0)
				: (BigDecimal) list.get(0);
		return count.longValue();
	}
	/**
	 * 
	 * @param session
	 * @param sql
	 * @param values
	 * @return
	 */
	private Long getDataTotalWithSql(Session session, String sql,
			Object[] values) {
		String countSQL = "select count(*) from (" + sql + ") tmp_count_t";
		Query query = session.createSQLQuery(countSQL);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		List<?> list = query.list();
		BigDecimal count = list == null || list.size() <= 0 ? new BigDecimal(0)
				: (BigDecimal) list.get(0);
		return count.longValue();
	}
	

	public Page<T> findByPageHash(final String entCode, final Page<T> page,
			final String hql, final Object... values) {
		try {
			// 如果设置获取总数，则先获取总数
			hibernateDaoSupport.getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					if (page.isAutoCount()) {
						Long li = getDataTotal(s, hql, values);
						int totalCount = 0;
						if (li != null) {
							totalCount = li.intValue();
						}
						page.setTotalCount(totalCount);// 设置总记录数
						page.getNextPage();// 是否还有下页
						page.getPrePage();// 是否还有上页
					}
					return page;
				}
			});
			return (Page<T>) hibernateDaoSupport.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							configInterceptor(entCode);
							Query query = createQuery(s, hql, values);
							if (page.isFirstSetted()) {
								query.setFirstResult(page.getFirst());
							}
							if (page.isPageSizeSetted()) {
								query.setMaxResults(page.getPageSize());
							}
							page.setResult(query.list());

							return page;
						}
					});
		} catch (RuntimeException e) {
			SysLogger.sysLogger.error("分页查询异常，HQL：" + hql, e);
			throw e;
		}
	}
	
	public void configInterceptor(String entCode) {
		SessionFactory sf = hibernateDaoSupport.getHibernateTemplate().getSessionFactory();
		if (sf instanceof SessionFactoryImplementor) {
			System.out.println("PageQuery sf implements SessionFactoryImplementor");
			SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sf;
			Interceptor interceptor = sessionFactoryImplementor
					.getInterceptor();
			if (interceptor instanceof CustomizeInterceptor) {
				System.out.println("PageQuery come in CustomizeInterceptor");
				CustomizeInterceptor customizeInterceptor = (CustomizeInterceptor) interceptor;
				customizeInterceptor.setSeed(entCode);
			}
		}
	}
}
