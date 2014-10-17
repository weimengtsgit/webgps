package com.sosgps.v21.dao.impl.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Interceptor;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.v21.dao.BaseDao;
import com.sosgps.v21.interceptor.CustomizeInterceptor;

public class BaseHibernateDao extends HibernateDaoSupport implements BaseDao {

	private static final Log log = LogFactory.getLog(BaseHibernateDao.class);

	final static int MAX_ARRAY_LEGTH = 1000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#setInjectionSessionFacotry
	 *      (org.hibernate.SessionFactory)
	 */
	public void setInjectionSessionFacotry(SessionFactory sessionFacotry) {
		super.setSessionFactory(sessionFacotry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#merge(java.lang.Object)
	 */
	public Object merge(Object detachedInstance) {
		log.debug("merging Object instance");
		try {
			Object result = getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#save(java.lang.Object)
	 */
	public Object save(Object transientInstance) {
		log.debug("saving Object instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
			return transientInstance;
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#saveOrUpdate(java.lang.Object )
	 */
	public Object saveOrUpdate(Object entity) {
		log.debug("saveOrUpdate Object instance");
		try {
			getHibernateTemplate().saveOrUpdate(entity);
			log.debug("saveOrUpdate successful");
			return entity;
		} catch (RuntimeException re) {
			log.error("saveOrUpdate failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#delete(java.lang.Object)
	 */
	public void delete(Object persistentInstance) {
		log.debug("deleting Object instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findById(java.lang.Long,
	 *      java.lang.Class)
	 */
	public Object findById(java.lang.Long id, Class<?> clazz) {
		log.debug("getting " + clazz.getName() + " instance with id: " + id);
		try {
			Object instance = getHibernateTemplate().get(clazz, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findById(int,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T findById(int id, Class<?> clazz) {
		log.debug("getting " + clazz.getName() + " instance with id: " + id);
		try {
			T instance = (T) getHibernateTemplate().get(clazz, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findById(java.lang.Number,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T findById(Number id, Class<?> clazz) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#attachDirty(java.lang.Object)
	 */
	public void attachDirty(Object instance) {
		log.debug("attaching dirty Object instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#attachClean(java.lang.Object)
	 */
	public void attachClean(Object instance) {
		log.debug("attaching clean Object instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#queryForInt(java.lang.String)
	 */
	public int queryForInt(String hql) {
		Object num = this.getHibernateTemplate().execute(
				new CommonHibernateCallback(hql, 0, 1));
		if (num != null) {
			Number number = (Number) num;
			return number.intValue();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#queryForInt(java.lang.String,
	 *      java.lang.Object[])
	 */
	public int queryForInt(String hql, Object[] values) {
		Object num = this.getHibernateTemplate().execute(
				new CommonHibernateCallback(hql, values, 0, 1));
		if (num != null) {
			Number number = (Number) num;
			return number.intValue();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#queryForInt(org.hibernate
	 *      .Query)
	 */
	public int queryForInt(Query query) {
		Object num = query.uniqueResult();
		if (num != null) {
			Number number = (Number) num;
			return number.intValue();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#update(java.lang.String,
	 *      java.lang.Object)
	 */
	public int update(String hql, Object... values) {
		return (Integer) getHibernateTemplate().execute(
				new CommonHibernateCallback(hql, values, false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#update(java.lang.Object)
	 */
	public void update(Object entity) {

		System.out.println(getHibernateTemplate().getSessionFactory()
				.getCurrentSession().getFlushMode());
		getHibernateTemplate().update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findList(java.lang.String,
	 *      java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findList(String hql, Object... values) {
		return getHibernateTemplate().find(hql, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findList(String hql) {
		return getHibernateTemplate().find(hql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findList(java.lang.String,
	 *      java.lang.Object[], int, int)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findList(String hql, Object[] values, int offset,
			int rowCount) {
		return getHibernateTemplate().executeFind(
				new CommonHibernateCallback(hql, values, offset, rowCount));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findList(java.lang.String,
	 *      int, int)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findList(String hql, int offset, int rowCount) {
		return getHibernateTemplate().executeFind(
				new CommonHibernateCallback(hql, offset, rowCount));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findObject(java.lang.String,
	 *      java.lang.Object)
	 */
	public <T> T findObject(String hql, Object... values) {
		List<T> list = this.findList(hql, values, 0, 1);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#makeExpression(java.lang.
	 *      String[])
	 */
	public String makeExpression(String[] properties) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < properties.length; i++) {
			sb.append(" AND " + properties[i] + "=?");
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findByProperty(java.lang.
	 *      Class, java.lang.String, java.lang.Object)
	 */
	public List<?> findByProperty(Class<?> clazz, String propertyName,
			Object propertyValue) {
		String hql = new StringBuffer("from ").append(clazz.getName()).append(
				" t where t.").append(propertyName).append("=?").toString();
		return getHibernateTemplate().find(hql, propertyValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findByExample(java.lang.Object )
	 */
	public List<?> findByExample(Object instance) {
		log.debug("finding TRole instance by example");
		try {
			List<?> results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.hibernate.BaseDao#findAll(java.lang.Class)
	 */
	public List<?> findAll(Class<?> clazz) {
		log.debug("finding all " + clazz.getSimpleName() + " instances");
		try {
			String queryString = "from " + clazz.getSimpleName();
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.multimedia.dao.BaseDao#excute(java.lang.String,
	 *      java.lang.Object[])
	 */
	public int excute(final String hql, final Object... values) {
		return getHibernateTemplate().bulkUpdate(hql, values);
	}

	public void configInterceptor(String entCode) {
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		if (sf instanceof SessionFactoryImplementor) {
			System.out.println("sf implements SessionFactoryImplementor");
			SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sf;
			Interceptor interceptor = sessionFactoryImplementor
					.getInterceptor();
			if (interceptor instanceof CustomizeInterceptor) {
				System.out.println("come in CustomizeInterceptor");
				CustomizeInterceptor customizeInterceptor = (CustomizeInterceptor) interceptor;
				customizeInterceptor.setSeed(entCode);
			}
		}
	}

}
