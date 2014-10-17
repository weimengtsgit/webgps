package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TJbWfdm.
 * 
 * @see com.sosgps.wzt.orm.TJbWfdm
 * @author MyEclipse Persistence Tools
 */

public class TJbWfdmDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TJbWfdmDAO.class);

	// property constants
	public static final String WFNR = "wfnr";

	protected void initDao() {
		// do nothing
	}

	public void save(TJbWfdm transientInstance) {
		log.debug("saving TJbWfdm instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TJbWfdm persistentInstance) {
		log.debug("deleting TJbWfdm instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TJbWfdm findById(java.lang.Long id) {
		log.debug("getting TJbWfdm instance with id: " + id);
		try {
			TJbWfdm instance = (TJbWfdm) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TJbWfdm", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TJbWfdm instance) {
		log.debug("finding TJbWfdm instance by example");
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
		log.debug("finding TJbWfdm instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TJbWfdm as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByWfnr(Object wfnr) {
		return findByProperty(WFNR, wfnr);
	}

	public List findAll() {
		log.debug("finding all TJbWfdm instances");
		try {
			String queryString = "from TJbWfdm";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TJbWfdm merge(TJbWfdm detachedInstance) {
		log.debug("merging TJbWfdm instance");
		try {
			TJbWfdm result = (TJbWfdm) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TJbWfdm instance) {
		log.debug("attaching dirty TJbWfdm instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TJbWfdm instance) {
		log.debug("attaching clean TJbWfdm instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TJbWfdmDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TJbWfdmDAO) ctx.getBean("TJbWfdmDAO");
	}
}