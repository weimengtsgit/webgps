package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TDistanceMonth.
 * 
 * @see com.sosgps.wzt.orm.TDistanceMonth
 * @author MyEclipse Persistence Tools
 */

public class TDistanceMonthDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TDistanceMonthDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String DISTANCE = "distance";

	protected void initDao() {
		// do nothing
	}

	public void save(TDistanceMonth transientInstance) {
		log.debug("saving TDistanceMonth instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TDistanceMonth persistentInstance) {
		log.debug("deleting TDistanceMonth instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TDistanceMonth findById(java.lang.Long id) {
		log.debug("getting TDistanceMonth instance with id: " + id);
		try {
			TDistanceMonth instance = (TDistanceMonth) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TDistanceMonth", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TDistanceMonth instance) {
		log.debug("finding TDistanceMonth instance by example");
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
		log.debug("finding TDistanceMonth instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TDistanceMonth as model where model."
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
		log.debug("finding all TDistanceMonth instances");
		try {
			String queryString = "from TDistanceMonth";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TDistanceMonth merge(TDistanceMonth detachedInstance) {
		log.debug("merging TDistanceMonth instance");
		try {
			TDistanceMonth result = (TDistanceMonth) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TDistanceMonth instance) {
		log.debug("attaching dirty TDistanceMonth instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TDistanceMonth instance) {
		log.debug("attaching clean TDistanceMonth instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TDistanceMonthDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TDistanceMonthDAO) ctx.getBean("TDistanceMonthDAO");
	}
}