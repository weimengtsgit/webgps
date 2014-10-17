package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * Configure entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.Configure
 * @author MyEclipse Persistence Tools
 */

public class ConfigureDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ConfigureDAO.class);
	// property constants
	public static final String _CKEY = "CKey";
	public static final String _CVALUE = "CValue";
	public static final String _CCOMMENTS = "CComments";

	protected void initDao() {
		// do nothing
	}

	public void save(Configure transientInstance) {
		log.debug("saving Configure instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Configure persistentInstance) {
		log.debug("deleting Configure instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Configure findById(java.lang.Long id) {
		log.debug("getting Configure instance with id: " + id);
		try {
			Configure instance = (Configure) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.Configure", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Configure instance) {
		log.debug("finding Configure instance by example");
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
		log.debug("finding Configure instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Configure as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCKey(Object CKey) {
		return findByProperty(_CKEY, CKey);
	}

	public List findByCValue(Object CValue) {
		return findByProperty(_CVALUE, CValue);
	}

	public List findByCComments(Object CComments) {
		return findByProperty(_CCOMMENTS, CComments);
	}

	public List findAll() {
		log.debug("finding all Configure instances");
		try {
			String queryString = "from Configure";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Configure merge(Configure detachedInstance) {
		log.debug("merging Configure instance");
		try {
			Configure result = (Configure) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Configure instance) {
		log.debug("attaching dirty Configure instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Configure instance) {
		log.debug("attaching clean Configure instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ConfigureDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ConfigureDAO) ctx.getBean("ConfigureDAO");
	}
}