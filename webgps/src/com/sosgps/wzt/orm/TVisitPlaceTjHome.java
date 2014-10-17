package com.sosgps.wzt.orm;

// Generated 2010-4-24 20:08:52 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TVisitPlaceTj.
 * @see com.sosgps.wzt.orm.TVisitPlaceTj
 * @author Hibernate Tools
 */

public class TVisitPlaceTjHome extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TVisitPlaceTjHome.class);

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

	public void save(TVisitPlaceTj transientInstance) {
		log.debug("saving TVisitPlaceTj instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TVisitPlaceTj persistentInstance) {
		log.debug("deleting TVisitPlaceTj instance");
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
		log.debug("finding TVisitPlaceTj instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TVisitPlaceTj as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TVisitPlaceTj findById(java.lang.Long id) {
		log.debug("getting TVisitPlaceTj instance with id: " + id);
		try {
			TVisitPlaceTj instance = (TVisitPlaceTj) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVisitPlaceTj", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	@SuppressWarnings("rawtypes")
	public List findByExample(TVisitPlaceTj instance) {
		log.debug("finding TVisitPlaceTj instance by example");
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
		log.debug("finding all TVisitPlaceTj instances");
		try {
			String queryString = "from TVisitPlaceTj";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVisitPlaceTj merge(TVisitPlaceTj detachedInstance) {
		log.debug("merging TVisitPlaceTj instance");
		try {
			TVisitPlaceTj result = (TVisitPlaceTj) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVisitPlaceTj instance) {
		log.debug("attaching dirty TVisitPlaceTj instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVisitPlaceTj instance) {
		log.debug("attaching clean TVisitPlaceTj instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TVisitPlaceTjHome getFromApplicationContext(ApplicationContext ctx) {
		return (TVisitPlaceTjHome) ctx.getBean("TVisitPlaceTjHome");
	}
}
