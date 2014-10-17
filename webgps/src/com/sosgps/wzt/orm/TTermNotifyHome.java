package com.sosgps.wzt.orm;

// Generated 2009-12-16 10:15:48 by Hibernate Tools 3.2.1.GA

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TTermNotify.
 * @see com.sosgps.wzt.orm.TTermNotify
 * @author Hibernate Tools
 */

public class TTermNotifyHome extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TTermNotifyHome.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String NOTIFYNUMBER = "notifyNumber";

	public static final String USERID = "userId";

	public static final String USERNAME = "userName";

	public static final String CREATEDATE = "createDate";

	public static final String ENTCODE = "entCode";

	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICEID, deviceId);
	}

	public List findByNotifyNumber(Object notifyNumber) {
		return findByProperty(NOTIFYNUMBER, notifyNumber);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USERID, userId);
	}

	public List findByUserName(Object userName) {
		return findByProperty(USERNAME, userName);
	}

	public List findByCreateDate(Object createDate) {
		return findByProperty(CREATEDATE, createDate);
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENTCODE, entCode);
	}

	public void save(TTermNotify transientInstance) {
		log.debug("saving TTermNotify instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TTermNotify persistentInstance) {
		log.debug("deleting TTermNotify instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TTermNotify instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TTermNotify as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TTermNotify findById(java.lang.Long id) {
		log.debug("getting TTermNotify instance with id: " + id);
		try {
			TTermNotify instance = (TTermNotify) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TTermNotify", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TTermNotify instance) {
		log.debug("finding TTermNotify instance by example");
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
		log.debug("finding all TTermNotify instances");
		try {
			String queryString = "from TTermNotify";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TTermNotify merge(TTermNotify detachedInstance) {
		log.debug("merging TTermNotify instance");
		try {
			TTermNotify result = (TTermNotify) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TTermNotify instance) {
		log.debug("attaching dirty TTermNotify instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TTermNotify instance) {
		log.debug("attaching clean TTermNotify instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TTermNotifyHome getFromApplicationContext(
			ApplicationContext ctx) {
		return (TTermNotifyHome) ctx.getBean("TTermNotifyHome");
	}
}
