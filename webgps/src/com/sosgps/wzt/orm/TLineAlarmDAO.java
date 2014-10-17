package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TLineAlarm.
 * 
 * @see com.sosgps.wzt.orm.TLineAlarm
 * @author MyEclipse Persistence Tools
 */

public class TLineAlarmDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TLineAlarmDAO.class);

	// property constants
	public static final String DEVICEID = "deviceid";

	public static final String ENTCODE = "entcode";

	public static final String RADIO = "radio";

	public static final String XYS = "xys";

	private static final String FLAG = "flag";

	protected void initDao() {
		// do nothing
	}

	public void save(TLineAlarm transientInstance) {
		log.debug("saving TLineAlarm instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TLineAlarm persistentInstance) {
		log.debug("deleting TLineAlarm instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TLineAlarm findById(java.lang.Long id) {
		log.debug("getting TLineAlarm instance with id: " + id);
		try {
			TLineAlarm instance = (TLineAlarm) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TLineAlarm", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TLineAlarm instance) {
		log.debug("finding TLineAlarm instance by example");
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
		log.debug("finding TLineAlarm instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TLineAlarm as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDeviceid(Object deviceid) {
		return findByProperty(DEVICEID, deviceid);
	}

	public List findByEntcode(Object entcode) {
		return findByProperty(ENTCODE, entcode);
	}

	public List findByRadio(Object radio) {
		return findByProperty(RADIO, radio);
	}

	public List findByXys(Object xys) {
		return findByProperty(XYS, xys);
	}
	public List findByFlag(Object flag) {
		return findByProperty(FLAG, flag);
	}
	public List findAll() {
		log.debug("finding all TLineAlarm instances");
		try {
			String queryString = "from TLineAlarm";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TLineAlarm merge(TLineAlarm detachedInstance) {
		log.debug("merging TLineAlarm instance");
		try {
			TLineAlarm result = (TLineAlarm) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TLineAlarm instance) {
		log.debug("attaching dirty TLineAlarm instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TLineAlarm instance) {
		log.debug("attaching clean TLineAlarm instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TLineAlarmDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TLineAlarmDAO) ctx.getBean("TLineAlarmDAO");
	}
}