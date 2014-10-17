package com.sosgps.wzt.orm;

// Generated 2010-4-10 9:20:50 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class RefTermAreaalarm.
 * @see com.sosgps.wzt.orm.RefTermAreaalarm
 * @author Hibernate Tools
 */

public class RefTermAreaalarmDao extends HibernateDaoSupport {

	private static final Log log = LogFactory
			.getLog(RefTermAreaalarmDao.class);

	//property constants
	public static final String ID = "id";

	public static final String TAREA = "TArea";

	public static final String TTERMINAL = "TTerminal";

	public List findByTArea(Object TArea) {
		return findByProperty(TAREA, TArea);
	}

	public List findByTTerminal(Object TTerminal) {
		return findByProperty(TTERMINAL, TTerminal);
	}

	public void save(RefTermAreaalarm transientInstance) {
		log.debug("saving RefTermAreaalarm instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RefTermAreaalarm persistentInstance) {
		log.debug("deleting RefTermAreaalarm instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding RefTermAreaalarm instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefTermAreaalarm as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public RefTermAreaalarm findById(java.lang.Long id) {
		log.debug("getting RefTermAreaalarm instance with id: " + id);
		try {
			RefTermAreaalarm instance = (RefTermAreaalarm) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.RefTermAreaalarm", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefTermAreaalarm instance) {
		log.debug("finding RefTermAreaalarm instance by example");
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
		log.debug("finding all RefTermAreaalarm instances");
		try {
			String queryString = "from RefTermAreaalarm";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefTermAreaalarm merge(RefTermAreaalarm detachedInstance) {
		log.debug("merging RefTermAreaalarm instance");
		try {
			RefTermAreaalarm result = (RefTermAreaalarm) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefTermAreaalarm instance) {
		log.debug("attaching dirty RefTermAreaalarm instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefTermAreaalarm instance) {
		log.debug("attaching clean RefTermAreaalarm instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefTermAreaalarmDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefTermAreaalarmDao) ctx.getBean("RefTermAreaalarmHome");
	}
}
