package com.sosgps.wzt.orm;


import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TempShortMessage entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TempShortMessage
 * @author MyEclipse Persistence Tools
 */

public class TempShortMessageDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TempShortMessageDAO.class);
	// property constants
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String CREATE_TIME = "createTime";
	public static final String USER_ID = "userId";
	public static final String DYNAMIC_PASSWORD = "dynamicPassword";

	protected void initDao() {
		// do nothing
	}

	public void save(TempShortMessage transientInstance) {
		log.debug("saving TempShortMessage instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public void update(TempShortMessage transientInstance) {
		log.debug("updating TempShortMessage instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	public void delete(TempShortMessage persistentInstance) {
		log.debug("deleting TempShortMessage instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TempShortMessage findById(java.lang.Long id) {
		log.debug("getting TempShortMessage instance with id: " + id);
		try {
			TempShortMessage instance = (TempShortMessage) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TempShortMessage", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TempShortMessage instance) {
		log.debug("finding TempShortMessage instance by example");
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
		log.debug("finding TempShortMessage instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TempShortMessage as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPhoneNumber(Object phoneNumber) {
		return findByProperty(PHONE_NUMBER, phoneNumber);
	}

	public List findByCreateTime(Object createTime) {
		return findByProperty(CREATE_TIME, createTime);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByDynamicPassword(Object dynamicPassword) {
		return findByProperty(DYNAMIC_PASSWORD, dynamicPassword);
	}

	public List findAll() {
		log.debug("finding all TempShortMessage instances");
		try {
			String queryString = "from TempShortMessage";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TempShortMessage merge(TempShortMessage detachedInstance) {
		log.debug("merging TempShortMessage instance");
		try {
			TempShortMessage result = (TempShortMessage) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TempShortMessage instance) {
		log.debug("attaching dirty TempShortMessage instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TempShortMessage instance) {
		log.debug("attaching clean TempShortMessage instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TempShortMessageDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TempShortMessageDAO) ctx.getBean("TempShortMessageDAO");
	}
}