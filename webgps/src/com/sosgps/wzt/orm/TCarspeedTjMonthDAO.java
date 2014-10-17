package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TCarspeedTjMonth.
 * 
 * @see com.sosgps.wzt.orm.TCarspeedTjMonth
 * @author MyEclipse Persistence Tools
 */

public class TCarspeedTjMonthDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TCarspeedTjMonthDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(TCarspeedTjMonth transientInstance) {
		log.debug("saving TCarspeedTjMonth instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TCarspeedTjMonth persistentInstance) {
		log.debug("deleting TCarspeedTjMonth instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TCarspeedTjMonth findById(java.lang.Long id) {
		log.debug("getting TCarspeedTjMonth instance with id: " + id);
		try {
			TCarspeedTjMonth instance = (TCarspeedTjMonth) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TCarspeedTjMonth", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TCarspeedTjMonth instance) {
		log.debug("finding TCarspeedTjMonth instance by example");
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
		log.debug("finding TCarspeedTjMonth instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TCarspeedTjMonth as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all TCarspeedTjMonth instances");
		try {
			String queryString = "from TCarspeedTjMonth";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TCarspeedTjMonth merge(TCarspeedTjMonth detachedInstance) {
		log.debug("merging TCarspeedTjMonth instance");
		try {
			TCarspeedTjMonth result = (TCarspeedTjMonth) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TCarspeedTjMonth instance) {
		log.debug("attaching dirty TCarspeedTjMonth instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TCarspeedTjMonth instance) {
		log.debug("attaching clean TCarspeedTjMonth instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TCarspeedTjMonthDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TCarspeedTjMonthDAO) ctx.getBean("TCarspeedTjMonthDAO");
	}
}