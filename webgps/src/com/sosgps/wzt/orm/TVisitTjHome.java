package com.sosgps.wzt.orm;

// Generated 2010-4-24 20:08:52 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TVisitTj.
 * @see com.sosgps.wzt.orm.TVisitTj
 * @author Hibernate Tools
 */

public class TVisitTjHome extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TVisitTjHome.class);

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

	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICEID, deviceId);
	}

	public List findByArriveTime(Object arriveTime) {
		return findByProperty(ARRIVETIME, arriveTime);
	}

	public List findByLeaveTime(Object leaveTime) {
		return findByProperty(LEAVETIME, leaveTime);
	}

	public List findByTjDate(Object tjDate) {
		return findByProperty(TJDATE, tjDate);
	}

	public List findByPoiId(Object poiId) {
		return findByProperty(POIID, poiId);
	}

	public List findByLocId(Object locId) {
		return findByProperty(LOCID, locId);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByJmx(Object jmx) {
		return findByProperty(JMX, jmx);
	}

	public List findByJmy(Object jmy) {
		return findByProperty(JMY, jmy);
	}

	public List findByLocDesc(Object locDesc) {
		return findByProperty(LOCDESC, locDesc);
	}

	public void save(TVisitTj transientInstance) {
		log.debug("saving TVisitTj instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TVisitTj persistentInstance) {
		log.debug("deleting TVisitTj instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TVisitTj instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TVisitTj as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TVisitTj findById(java.lang.Long id) {
		log.debug("getting TVisitTj instance with id: " + id);
		try {
			TVisitTj instance = (TVisitTj) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVisitTj", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TVisitTj instance) {
		log.debug("finding TVisitTj instance by example");
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

	public List findAll() {
		log.debug("finding all TVisitTj instances");
		try {
			String queryString = "from TVisitTj";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVisitTj merge(TVisitTj detachedInstance) {
		log.debug("merging TVisitTj instance");
		try {
			TVisitTj result = (TVisitTj) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVisitTj instance) {
		log.debug("attaching dirty TVisitTj instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVisitTj instance) {
		log.debug("attaching clean TVisitTj instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TVisitTjHome getFromApplicationContext(ApplicationContext ctx) {
		return (TVisitTjHome) ctx.getBean("TVisitTjHome");
	}
}
