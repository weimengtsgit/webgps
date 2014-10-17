package com.sosgps.wzt.manage.util;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @Title:DAO操作基类
 * @Description:本DAO层实现了通用的数据操作
 * @param <T>
 *            POJO实体对象
 * @param <ID>
 *            ID
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-4 下午03:53:02
 */
public class BaseHibernateDAO<T, ID extends Serializable> extends
		HibernateDaoSupport {
	private static final Logger logger = Logger
			.getLogger(BaseHibernateDAO.class);

	/**
	 * * 按HQL分页查询.
	 * 
	 * @param page
	 *            页面对象
	 * @param hql
	 *            HQL语句
	 * @param values
	 *            可变参数列表
	 * @return 分页数据
	 */
	public Page<T> findByPage(final Page<T> page, final String hql,
			final Object... values) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("开始查找指定HQL分页数据," + hql);
			}
			// 如果设置获取总数，则先获取总数
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					String countHql = hql.substring(hql.indexOf("from"));
					countHql = "select count(*) " + countHql;
					Query query = createQuery(s, hql, values);
					if (page.isAutoCount()) {
						List li = query.list();
						int totalCount = 0;
						if (li != null) {
							totalCount = ((Long)li.get(0)).intValue();
						}
						page.setTotalCount(totalCount);// 设置总记录数
						page.getNextPage();// 是否还有下页
						page.getPrePage();// 是否还有上页
					}
					return page;
				}
			});
			return (Page<T>) getHibernateTemplate().execute(
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
							if (logger.isDebugEnabled()) {
								logger.debug("查找指定HQL分页数据成功," + hql);
							}
							return page;
						}
					});
		} catch (RuntimeException e) {
			logger.error("分页查询异常，HQL：" + hql, e);
			throw e;
		}
	}

	/**
	 * 根据查询条件与参数列表创建Query对象
	 * 
	 * @param session
	 *            Hibernate会话
	 * @param hql
	 *            HQL语句
	 * @param objects
	 *            参数列表
	 * @return Query对象
	 */
	public Query createQuery(Session session, String hql, Object... objects) {
		Query query = session.createQuery(hql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}
}
