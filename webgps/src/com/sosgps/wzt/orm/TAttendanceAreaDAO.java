package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TAttendanceArea entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TAttendanceArea
 * @author MyEclipse Persistence Tools
 */

public class TAttendanceAreaDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TAttendanceAreaDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String POINTS = "points";
	public static final String ENT_CODE = "entCode";

	protected void initDao() {
		// do nothing
	}

	public void save(TAttendanceArea transientInstance) {
		log.debug("saving TAttendanceArea instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void update(TAttendanceArea transientInstance) {
		log.debug("update TAttendanceArea instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	public void delete(TAttendanceArea persistentInstance) {
		log.debug("deleting TAttendanceArea instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TAttendanceArea findById(java.lang.Long id) {
		log.debug("getting TAttendanceArea instance with id: " + id);
		try {
			TAttendanceArea instance = (TAttendanceArea) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TAttendanceArea", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TAttendanceArea instance) {
		log.debug("finding TAttendanceArea instance by example");
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
		log.debug("finding TAttendanceArea instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TAttendanceArea as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByPoints(Object points) {
		return findByProperty(POINTS, points);
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public List findAll() {
		log.debug("finding all TAttendanceArea instances");
		try {
			String queryString = "from TAttendanceArea";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAttendanceArea merge(TAttendanceArea detachedInstance) {
		log.debug("merging TAttendanceArea instance");
		try {
			TAttendanceArea result = (TAttendanceArea) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAttendanceArea instance) {
		log.debug("attaching dirty TAttendanceArea instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAttendanceArea instance) {
		log.debug("attaching clean TAttendanceArea instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TAttendanceAreaDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TAttendanceAreaDAO) ctx.getBean("TAttendanceAreaDAO");
	}
}