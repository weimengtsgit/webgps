package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TCarspeedTjDay.
 * 
 * @see com.sosgps.wzt.orm.TCarspeedTjDay
 * @author MyEclipse Persistence Tools
 */

public class TCarspeedTjDayDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TCarspeedTjDayDAO.class);

	protected void initDao() {
		// do nothing
	}

	public void save(TCarspeedTjDay transientInstance) {
		log.debug("saving TCarspeedTjDay instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TCarspeedTjDay persistentInstance) {
		log.debug("deleting TCarspeedTjDay instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TCarspeedTjDay findById(java.lang.Long id) {
		log.debug("getting TCarspeedTjDay instance with id: " + id);
		try {
			TCarspeedTjDay instance = (TCarspeedTjDay) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TCarspeedTjDay", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TCarspeedTjDay instance) {
		log.debug("finding TCarspeedTjDay instance by example");
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
		log.debug("finding TCarspeedTjDay instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TCarspeedTjDay as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all TCarspeedTjDay instances");
		try {
			String queryString = "from TCarspeedTjDay";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TCarspeedTjDay merge(TCarspeedTjDay detachedInstance) {
		log.debug("merging TCarspeedTjDay instance");
		try {
			TCarspeedTjDay result = (TCarspeedTjDay) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TCarspeedTjDay instance) {
		log.debug("attaching dirty TCarspeedTjDay instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TCarspeedTjDay instance) {
		log.debug("attaching clean TCarspeedTjDay instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TCarspeedTjDayDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TCarspeedTjDayDAO) ctx.getBean("TCarspeedTjDayDAO");
	}
}