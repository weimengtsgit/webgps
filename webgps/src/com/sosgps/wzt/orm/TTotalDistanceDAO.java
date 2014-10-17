package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TTotalDistance.
 * 
 * @see com.sosgps.wzt.orm.TTotalDistance
 * @author MyEclipse Persistence Tools
 */

public class TTotalDistanceDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TTotalDistanceDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String DISTANCE = "distance";

	protected void initDao() {
		// do nothing
	}

	public void save(TTotalDistance transientInstance) {
		log.debug("saving TTotalDistance instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TTotalDistance persistentInstance) {
		log.debug("deleting TTotalDistance instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TTotalDistance findById(java.lang.Long id) {
		log.debug("getting TTotalDistance instance with id: " + id);
		try {
			TTotalDistance instance = (TTotalDistance) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TTotalDistance", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TTotalDistance instance) {
		log.debug("finding TTotalDistance instance by example");
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
		log.debug("finding TTotalDistance instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TTotalDistance as model where model."
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
		log.debug("finding all TTotalDistance instances");
		try {
			String queryString = "from TTotalDistance";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TTotalDistance merge(TTotalDistance detachedInstance) {
		log.debug("merging TTotalDistance instance");
		try {
			TTotalDistance result = (TTotalDistance) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TTotalDistance instance) {
		log.debug("attaching dirty TTotalDistance instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TTotalDistance instance) {
		log.debug("attaching clean TTotalDistance instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TTotalDistanceDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TTotalDistanceDAO) ctx.getBean("TTotalDistanceDAO");
	}
}