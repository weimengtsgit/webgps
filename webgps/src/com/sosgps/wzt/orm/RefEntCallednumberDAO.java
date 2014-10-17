package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * RefEntCallednumber entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.RefEntCallednumber
 * @author MyEclipse Persistence Tools
 */

public class RefEntCallednumberDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(RefEntCallednumberDAO.class);
	// property constants
	public static final String CALLED_NUMBER = "calledNumber";

	protected void initDao() {
		// do nothing
	}

	public void save(RefEntCallednumber transientInstance) {
		log.debug("saving RefEntCallednumber instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void update(RefEntCallednumber transientInstance) {
		log.debug("update RefEntCallednumber instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	public void delete(RefEntCallednumber persistentInstance) {
		log.debug("deleting RefEntCallednumber instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RefEntCallednumber findById(java.lang.Long id) {
		log.debug("getting RefEntCallednumber instance with id: " + id);
		try {
			RefEntCallednumber instance = (RefEntCallednumber) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.RefEntCallednumber", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefEntCallednumber instance) {
		log.debug("finding RefEntCallednumber instance by example");
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
		log.debug("finding RefEntCallednumber instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefEntCallednumber as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCalledNumber(Object calledNumber) {
		return findByProperty(CALLED_NUMBER, calledNumber);
	}

	public List findAll() {
		log.debug("finding all RefEntCallednumber instances");
		try {
			String queryString = "from RefEntCallednumber";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefEntCallednumber merge(RefEntCallednumber detachedInstance) {
		log.debug("merging RefEntCallednumber instance");
		try {
			RefEntCallednumber result = (RefEntCallednumber) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefEntCallednumber instance) {
		log.debug("attaching dirty RefEntCallednumber instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefEntCallednumber instance) {
		log.debug("attaching clean RefEntCallednumber instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefEntCallednumberDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefEntCallednumberDAO) ctx.getBean("RefEntCallednumberDAO");
	}
}