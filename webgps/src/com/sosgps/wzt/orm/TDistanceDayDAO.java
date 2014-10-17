package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TDistanceDay.
 * 
 * @see com.sosgps.wzt.orm.TDistanceDay
 * @author MyEclipse Persistence Tools
 */

public class TDistanceDayDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TDistanceDayDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String DISTANCE = "distance";

	protected void initDao() {
		// do nothing
	}

	public void save(TDistanceDay transientInstance) {
		log.debug("saving TDistanceDay instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TDistanceDay persistentInstance) {
		log.debug("deleting TDistanceDay instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TDistanceDay findById(java.lang.Long id) {
		log.debug("getting TDistanceDay instance with id: " + id);
		try {
			TDistanceDay instance = (TDistanceDay) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TDistanceDay", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TDistanceDay instance) {
		log.debug("finding TDistanceDay instance by example");
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
		log.debug("finding TDistanceDay instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TDistanceDay as model where model."
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
		log.debug("finding all TDistanceDay instances");
		try {
			String queryString = "from TDistanceDay";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TDistanceDay merge(TDistanceDay detachedInstance) {
		log.debug("merging TDistanceDay instance");
		try {
			TDistanceDay result = (TDistanceDay) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TDistanceDay instance) {
		log.debug("attaching dirty TDistanceDay instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TDistanceDay instance) {
		log.debug("attaching clean TDistanceDay instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TDistanceDayDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TDistanceDayDAO) ctx.getBean("TDistanceDayDAO");
	}
}