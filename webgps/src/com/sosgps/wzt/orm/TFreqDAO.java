package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TFreq.
 * 
 * @see com.sosgps.wzt.orm.TFreq
 * @author MyEclipse Persistence Tools
 */

public class TFreqDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TFreqDAO.class);

	// property constants
	public static final String VALUE = "value";

	public static final String DEVICE_ID = "deviceId";

	public static final String CRTMAN = "crtman";

	protected void initDao() {
		// do nothing
	}

	public void save(TFreq transientInstance) {
		log.debug("saving TFreq instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TFreq persistentInstance) {
		log.debug("deleting TFreq instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TFreq findById(java.lang.Long id) {
		log.debug("getting TFreq instance with id: " + id);
		try {
			TFreq instance = (TFreq) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TFreq", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TFreq instance) {
		log.debug("finding TFreq instance by example");
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
		log.debug("finding TFreq instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TFreq as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByValue(Object value) {
		return findByProperty(VALUE, value);
	}

	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICE_ID, deviceId);
	}

	public List findByCrtman(Object crtman) {
		return findByProperty(CRTMAN, crtman);
	}

	public List findAll() {
		log.debug("finding all TFreq instances");
		try {
			String queryString = "from TFreq";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TFreq merge(TFreq detachedInstance) {
		log.debug("merging TFreq instance");
		try {
			TFreq result = (TFreq) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TFreq instance) {
		log.debug("attaching dirty TFreq instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TFreq instance) {
		log.debug("attaching clean TFreq instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TFreqDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TFreqDAO) ctx.getBean("TFreqDAO");
	}
}