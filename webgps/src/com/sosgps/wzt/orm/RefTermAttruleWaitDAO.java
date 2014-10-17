package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * RefTermAttruleWait entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.RefTermAttruleWait
 * @author MyEclipse Persistence Tools
 */

public class RefTermAttruleWaitDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(RefTermAttruleWaitDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(RefTermAttruleWait transientInstance) {
		log.debug("saving RefTermAttruleWait instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RefTermAttruleWait persistentInstance) {
		log.debug("deleting RefTermAttruleWait instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RefTermAttruleWait findById(com.sosgps.wzt.orm.RefTermAttruleWaitId id) {
		log.debug("getting RefTermAttruleWait instance with id: " + id);
		try {
			RefTermAttruleWait instance = (RefTermAttruleWait) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.RefTermAttruleWait", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefTermAttruleWait instance) {
		log.debug("finding RefTermAttruleWait instance by example");
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
		log.debug("finding RefTermAttruleWait instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefTermAttruleWait as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all RefTermAttruleWait instances");
		try {
			String queryString = "from RefTermAttruleWait";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefTermAttruleWait merge(RefTermAttruleWait detachedInstance) {
		log.debug("merging RefTermAttruleWait instance");
		try {
			RefTermAttruleWait result = (RefTermAttruleWait) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefTermAttruleWait instance) {
		log.debug("attaching dirty RefTermAttruleWait instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefTermAttruleWait instance) {
		log.debug("attaching clean RefTermAttruleWait instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefTermAttruleWaitDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefTermAttruleWaitDAO) ctx.getBean("RefTermAttruleWaitDAO");
	}
}