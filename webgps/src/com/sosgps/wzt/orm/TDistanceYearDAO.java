package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TDistanceYear.
 * 
 * @see com.sosgps.wzt.orm.TDistanceYear
 * @author MyEclipse Persistence Tools
 */

public class TDistanceYearDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TDistanceYearDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String DISTANCE = "distance";

	protected void initDao() {
		// do nothing
	}

	public void save(TDistanceYear transientInstance) {
		log.debug("saving TDistanceYear instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TDistanceYear persistentInstance) {
		log.debug("deleting TDistanceYear instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TDistanceYear findById(java.lang.Long id) {
		log.debug("getting TDistanceYear instance with id: " + id);
		try {
			TDistanceYear instance = (TDistanceYear) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TDistanceYear", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TDistanceYear instance) {
		log.debug("finding TDistanceYear instance by example");
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
		log.debug("finding TDistanceYear instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TDistanceYear as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICE_ID, deviceId);
	}

	public List findByDistance(Object distance) {
		return findByProperty(DISTANCE, distance);
	}

	public List findAll() {
		log.debug("finding all TDistanceYear instances");
		try {
			String queryString = "from TDistanceYear";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TDistanceYear merge(TDistanceYear detachedInstance) {
		log.debug("merging TDistanceYear instance");
		try {
			TDistanceYear result = (TDistanceYear) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TDistanceYear instance) {
		log.debug("attaching dirty TDistanceYear instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TDistanceYear instance) {
		log.debug("attaching clean TDistanceYear instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TDistanceYearDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TDistanceYearDAO) ctx.getBean("TDistanceYearDAO");
	}
}