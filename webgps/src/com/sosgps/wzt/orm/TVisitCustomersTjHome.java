package com.sosgps.wzt.orm;

// Generated 2010-4-24 20:08:52 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TVisitCustomersTj.
 * @see com.sosgps.wzt.orm.TVisitCustomersTj
 * @author Hibernate Tools
 */

public class TVisitCustomersTjHome extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TVisitCustomersTjHome.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String ARRIVETIME = "arriveTime";

	public static final String LEAVETIME = "leaveTime";

	public static final String TJDATE = "tjDate";

	public static final String POIID = "poiId";

	public static final String LOCID = "locId";

	public static final String LONGITUDE = "longitude";

	public static final String LATITUDE = "latitude";

	public static final String JMX = "jmx";

	public static final String JMY = "jmy";

	public static final String LOCDESC = "locDesc";
	@SuppressWarnings("rawtypes")
	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICEID, deviceId);
	}
	@SuppressWarnings("rawtypes")
	public List findByArriveTime(Object arriveTime) {
		return findByProperty(ARRIVETIME, arriveTime);
	}
	@SuppressWarnings("rawtypes")
	public List findByLeaveTime(Object leaveTime) {
		return findByProperty(LEAVETIME, leaveTime);
	}
	@SuppressWarnings("rawtypes")
	public List findByTjDate(Object tjDate) {
		return findByProperty(TJDATE, tjDate);
	}
	@SuppressWarnings("rawtypes")
	public List findByPoiId(Object poiId) {
		return findByProperty(POIID, poiId);
	}
	@SuppressWarnings("rawtypes")
	public List findByLocId(Object locId) {
		return findByProperty(LOCID, locId);
	}
	@SuppressWarnings("rawtypes")
	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}
	@SuppressWarnings("rawtypes")
	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}
	@SuppressWarnings("rawtypes")
	public List findByJmx(Object jmx) {
		return findByProperty(JMX, jmx);
	}
	@SuppressWarnings("rawtypes")
	public List findByJmy(Object jmy) {
		return findByProperty(JMY, jmy);
	}
	@SuppressWarnings("rawtypes")
	public List findByLocDesc(Object locDesc) {
		return findByProperty(LOCDESC, locDesc);
	}

	public void save(TVisitCustomersTj transientInstance) {
		log.debug("saving TVisitCustomersTj instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TVisitCustomersTj persistentInstance) {
		log.debug("deleting TVisitCustomersTj instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	@SuppressWarnings("rawtypes")
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TVisitCustomersTj instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TVisitCustomersTj as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TVisitCustomersTj findById(java.lang.Long id) {
		log.debug("getting TVisitCustomersTj instance with id: " + id);
		try {
			TVisitCustomersTj instance = (TVisitCustomersTj) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVisitCustomersTj", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	@SuppressWarnings("rawtypes")
	public List findByExample(TVisitCustomersTj instance) {
		log.debug("finding TVisitCustomersTj instance by example");
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
	@SuppressWarnings("rawtypes")
	public List findAll() {
		log.debug("finding all TVisitCustomersTj instances");
		try {
			String queryString = "from TVisitCustomersTj";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVisitCustomersTj merge(TVisitCustomersTj detachedInstance) {
		log.debug("merging TVisitCustomersTj instance");
		try {
			TVisitCustomersTj result = (TVisitCustomersTj) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVisitCustomersTj instance) {
		log.debug("attaching dirty TVisitCustomersTj instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVisitCustomersTj instance) {
		log.debug("attaching clean TVisitCustomersTj instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TVisitCustomersTjHome getFromApplicationContext(ApplicationContext ctx) {
		return (TVisitCustomersTjHome) ctx.getBean("TVisitCustomersTjHomeHome");
	}
}
