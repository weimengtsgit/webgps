package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TCarspeedTjYear.
 * 
 * @see com.sosgps.wzt.orm.TCarspeedTjYear
 * @author MyEclipse Persistence Tools
 */

public class TCarspeedTjYearDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TCarspeedTjYearDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(TCarspeedTjYear transientInstance) {
		log.debug("saving TCarspeedTjYear instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TCarspeedTjYear persistentInstance) {
		log.debug("deleting TCarspeedTjYear instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TCarspeedTjYear findById(java.lang.Long id) {
		log.debug("getting TCarspeedTjYear instance with id: " + id);
		try {
			TCarspeedTjYear instance = (TCarspeedTjYear) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TCarspeedTjYear", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TCarspeedTjYear instance) {
		log.debug("finding TCarspeedTjYear instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TCarspeedTjYear instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TCarspeedTjYear as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all TCarspeedTjYear instances");
		try {
			String queryString = "from TCarspeedTjYear";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TCarspeedTjYear merge(TCarspeedTjYear detachedInstance) {
		log.debug("merging TCarspeedTjYear instance");
		try {
			TCarspeedTjYear result = (TCarspeedTjYear) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TCarspeedTjYear instance) {
		log.debug("attaching dirty TCarspeedTjYear instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TCarspeedTjYear instance) {
		log.debug("attaching clean TCarspeedTjYear instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TCarspeedTjYearDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TCarspeedTjYearDAO) ctx.getBean("TCarspeedTjYearDAO");
	}
}