package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TMutiMedium.
 * 
 * @see com.sosgps.wzt.orm.TMutiMedium
 * @author MyEclipse Persistence Tools
 */

public class TMutiMediumDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TMutiMediumDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String IMAGE = "image";

	public static final String LANTITUDE = "lantitude";

	public static final String LONGITUDE = "longitude";

	public static final String JMX = "jmx";

	public static final String JMY = "jmy";

	public static final String IMG_TYPE = "imgType";

	protected void initDao() {
		// do nothing
	}

	public void save(TMutiMedium transientInstance) {
		log.debug("saving TMutiMedium instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TMutiMedium persistentInstance) {
		log.debug("deleting TMutiMedium instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TMutiMedium findById(java.lang.Long id) {
		log.debug("getting TMutiMedium instance with id: " + id);
		try {
			TMutiMedium instance = (TMutiMedium) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TMutiMedium", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TMutiMedium instance) {
		log.debug("finding TMutiMedium instance by example");
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
		log.debug("finding TMutiMedium instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TMutiMedium as model where model."
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

	public List findByImage(Object image) {
		return findByProperty(IMAGE, image);
	}

	public List findByLantitude(Object lantitude) {
		return findByProperty(LANTITUDE, lantitude);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByJmx(Object jmx) {
		return findByProperty(JMX, jmx);
	}

	public List findByJmy(Object jmy) {
		return findByProperty(JMY, jmy);
	}

	public List findByImgType(Object imgType) {
		return findByProperty(IMG_TYPE, imgType);
	}

	public List findAll() {
		log.debug("finding all TMutiMedium instances");
		try {
			String queryString = "from TMutiMedium";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TMutiMedium merge(TMutiMedium detachedInstance) {
		log.debug("merging TMutiMedium instance");
		try {
			TMutiMedium result = (TMutiMedium) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TMutiMedium instance) {
		log.debug("attaching dirty TMutiMedium instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TMutiMedium instance) {
		log.debug("attaching clean TMutiMedium instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TMutiMediumDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TMutiMediumDAO) ctx.getBean("TMutiMediumDAO");
	}
}