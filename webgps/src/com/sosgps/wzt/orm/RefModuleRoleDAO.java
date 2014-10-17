package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * RefModuleRole entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.RefModuleRole
 * @author MyEclipse Persistence Tools
 */

public class RefModuleRoleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(RefModuleRoleDAO.class);
	// property constants
	public static final String MODULE_ORDER = "moduleOrder";

	protected void initDao() {
		// do nothing
	}

	public void save(RefModuleRole transientInstance) {
		log.debug("saving RefModuleRole instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void update(RefModuleRole transientInstance) {
		log.debug("update RefModuleRole instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	public void delete(RefModuleRole persistentInstance) {
		log.debug("deleting RefModuleRole instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RefModuleRole findById(java.lang.Long id) {
		log.debug("getting RefModuleRole instance with id: " + id);
		try {
			RefModuleRole instance = (RefModuleRole) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.RefModuleRole", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefModuleRole instance) {
		log.debug("finding RefModuleRole instance by example");
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
		log.debug("finding RefModuleRole instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefModuleRole as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByModuleOrder(Object moduleOrder) {
		return findByProperty(MODULE_ORDER, moduleOrder);
	}

	public List findAll() {
		log.debug("finding all RefModuleRole instances");
		try {
			String queryString = "from RefModuleRole";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefModuleRole merge(RefModuleRole detachedInstance) {
		log.debug("merging RefModuleRole instance");
		try {
			RefModuleRole result = (RefModuleRole) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefModuleRole instance) {
		log.debug("attaching dirty RefModuleRole instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefModuleRole instance) {
		log.debug("attaching clean RefModuleRole instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefModuleRoleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefModuleRoleDAO) ctx.getBean("RefModuleRoleDAO");
	}
}