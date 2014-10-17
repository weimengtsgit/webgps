package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TAlarmShow.
 * 
 * @see com.sosgps.wzt.orm.TAlarmShow
 * @author MyEclipse Persistence Tools
 */

public class TAlarmShowDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TAlarmShowDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String ALARM_TYPE = "alarmType";

	public static final String ALARM_CONTENT = "alarmContent";

	public static final String FLAG = "flag";

	public static final String ALARM_ID = "alarmId";

	protected void initDao() {
		// do nothing
	}

	public void save(TAlarmShow transientInstance) {
		log.debug("saving TAlarmShow instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TAlarmShow persistentInstance) {
		log.debug("deleting TAlarmShow instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TAlarmShow findById(java.lang.Long id) {
		log.debug("getting TAlarmShow instance with id: " + id);
		try {
			TAlarmShow instance = (TAlarmShow) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TAlarmShow", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TAlarmShow instance) {
		log.debug("finding TAlarmShow instance by example");
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
		log.debug("finding TAlarmShow instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TAlarmShow as model where model."
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

	public List findByAlarmType(Object alarmType) {
		return findByProperty(ALARM_TYPE, alarmType);
	}

	public List findByAlarmContent(Object alarmContent) {
		return findByProperty(ALARM_CONTENT, alarmContent);
	}

	public List findByFlag(Object flag) {
		return findByProperty(FLAG, flag);
	}

	public List findByAlarmId(Object alarmId) {
		return findByProperty(ALARM_ID, alarmId);
	}

	public List findAll() {
		log.debug("finding all TAlarmShow instances");
		try {
			String queryString = "from TAlarmShow";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAlarmShow merge(TAlarmShow detachedInstance) {
		log.debug("merging TAlarmShow instance");
		try {
			TAlarmShow result = (TAlarmShow) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAlarmShow instance) {
		log.debug("attaching dirty TAlarmShow instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAlarmShow instance) {
		log.debug("attaching clean TAlarmShow instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TAlarmShowDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TAlarmShowDAO) ctx.getBean("TAlarmShowDAO");
	}
}