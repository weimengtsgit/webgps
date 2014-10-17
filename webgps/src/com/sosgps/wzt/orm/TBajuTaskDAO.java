package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TBajuTask.
 * 
 * @see com.sosgps.wzt.orm.TBajuTask
 * @author MyEclipse Persistence Tools
 */

public class TBajuTaskDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TBajuTaskDAO.class);

	// property constants
	public static final String DEVICE_ID = "deviceId";

	public static final String TASK_CONTENT = "taskContent";

	public static final String STATE = "state";

	public static final String TYPE = "type";

	public static final String REPLY = "reply";

	public static final String CRTMAN = "crtman";

	protected void initDao() {
		// do nothing
	}

	public void save(TBajuTask transientInstance) {
		log.debug("saving TBajuTask instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TBajuTask persistentInstance) {
		log.debug("deleting TBajuTask instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TBajuTask findById(java.lang.Long id) {
		log.debug("getting TBajuTask instance with id: " + id);
		try {
			TBajuTask instance = (TBajuTask) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TBajuTask", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TBajuTask instance) {
		log.debug("finding TBajuTask instance by example");
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
		log.debug("finding TBajuTask instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TBajuTask as model where model."
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

	public List findByTaskContent(Object taskContent) {
		return findByProperty(TASK_CONTENT, taskContent);
	}

	public List findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByReply(Object reply) {
		return findByProperty(REPLY, reply);
	}

	public List findByCrtman(Object crtman) {
		return findByProperty(CRTMAN, crtman);
	}

	public List findAll() {
		log.debug("finding all TBajuTask instances");
		try {
			String queryString = "from TBajuTask";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TBajuTask merge(TBajuTask detachedInstance) {
		log.debug("merging TBajuTask instance");
		try {
			TBajuTask result = (TBajuTask) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TBajuTask instance) {
		log.debug("attaching dirty TBajuTask instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TBajuTask instance) {
		log.debug("attaching clean TBajuTask instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TBajuTaskDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TBajuTaskDAO) ctx.getBean("TBajuTaskDAO");
	}
}