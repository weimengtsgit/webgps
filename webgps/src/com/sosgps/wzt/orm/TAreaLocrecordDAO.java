package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TAreaLocrecord.
 * 
 * @see com.sosgps.wzt.orm.TAreaLocrecord
 * @author MyEclipse Persistence Tools
 */

public class TAreaLocrecordDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TAreaLocrecordDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String LONGITUDE = "longitude";

	public static final String LATITUDE = "latitude";

	public static final String JMX = "jmx";

	public static final String JMY = "jmy";

	public static final String SPEED = "speed";

	public static final String DIRECTION = "direction";

	public static final String HEIGHT = "height";

	public static final String DISTANCE = "distance";

	public static final String STATLLITE_NUM = "statlliteNum";

	public static final String ALARM_TYPE = "alarmType";

	public static final String CASE_ID = "caseId";

	protected void initDao() {
		// do nothing
	}

	public void save(TAreaLocrecord transientInstance) {
		log.debug("saving TAreaLocrecord instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TAreaLocrecord persistentInstance) {
		log.debug("deleting TAreaLocrecord instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TAreaLocrecord findById(java.lang.Long id) {
		log.debug("getting TAreaLocrecord instance with id: " + id);
		try {
			TAreaLocrecord instance = (TAreaLocrecord) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TAreaLocrecord", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TAreaLocrecord instance) {
		log.debug("finding TAreaLocrecord instance by example");
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
		log.debug("finding TAreaLocrecord instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TAreaLocrecord as model where model."
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

	public List findBySpeed(Object speed) {
		return findByProperty(SPEED, speed);
	}

	public List findByDirection(Object direction) {
		return findByProperty(DIRECTION, direction);
	}

	public List findByHeight(Object height) {
		return findByProperty(HEIGHT, height);
	}

	public List findByDistance(Object distance) {
		return findByProperty(DISTANCE, distance);
	}

	public List findByStatlliteNum(Object statlliteNum) {
		return findByProperty(STATLLITE_NUM, statlliteNum);
	}

	public List findByAlarmType(Object alarmType) {
		return findByProperty(ALARM_TYPE, alarmType);
	}

	public List findByCaseId(Object caseId) {
		return findByProperty(CASE_ID, caseId);
	}

	public List findAll() {
		log.debug("finding all TAreaLocrecord instances");
		try {
			String queryString = "from TAreaLocrecord";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAreaLocrecord merge(TAreaLocrecord detachedInstance) {
		log.debug("merging TAreaLocrecord instance");
		try {
			TAreaLocrecord result = (TAreaLocrecord) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAreaLocrecord instance) {
		log.debug("attaching dirty TAreaLocrecord instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAreaLocrecord instance) {
		log.debug("attaching clean TAreaLocrecord instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TAreaLocrecordDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TAreaLocrecordDAO) ctx.getBean("TAreaLocrecordDAO");
	}
}