package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * RefTermAttrule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.RefTermAttrule
 * @author MyEclipse Persistence Tools
 */

public class RefTermAttruleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(RefTermAttruleDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(RefTermAttrule transientInstance) {
		log.debug("saving RefTermAttrule instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RefTermAttrule persistentInstance) {
		log.debug("deleting RefTermAttrule instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RefTermAttrule findById(com.sosgps.wzt.orm.RefTermAttruleId id) {
		log.debug("getting RefTermAttrule instance with id: " + id);
		try {
			RefTermAttrule instance = (RefTermAttrule) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.RefTermAttrule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefTermAttrule instance) {
		log.debug("finding RefTermAttrule instance by example");
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
		log.debug("finding RefTermAttrule instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefTermAttrule as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all RefTermAttrule instances");
		try {
			String queryString = "from RefTermAttrule";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefTermAttrule merge(RefTermAttrule detachedInstance) {
		log.debug("merging RefTermAttrule instance");
		try {
			RefTermAttrule result = (RefTermAttrule) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefTermAttrule instance) {
		log.debug("attaching dirty RefTermAttrule instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefTermAttrule instance) {
		log.debug("attaching clean RefTermAttrule instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefTermAttruleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefTermAttruleDAO) ctx.getBean("RefTermAttruleDAO");
	}
}