package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * RefTermSimrule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.RefTermSimrule
 * @author MyEclipse Persistence Tools
 */

public class RefTermSimruleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(RefTermSimruleDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(RefTermSimrule transientInstance) {
		log.debug("saving RefTermSimrule instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RefTermSimrule persistentInstance) {
		log.debug("deleting RefTermSimrule instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RefTermSimrule findById(com.sosgps.wzt.orm.RefTermSimruleId id) {
		log.debug("getting RefTermSimrule instance with id: " + id);
		try {
			RefTermSimrule instance = (RefTermSimrule) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.RefTermSimrule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefTermSimrule instance) {
		log.debug("finding RefTermSimrule instance by example");
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
		log.debug("finding RefTermSimrule instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefTermSimrule as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all RefTermSimrule instances");
		try {
			String queryString = "from RefTermSimrule";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefTermSimrule merge(RefTermSimrule detachedInstance) {
		log.debug("merging RefTermSimrule instance");
		try {
			RefTermSimrule result = (RefTermSimrule) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefTermSimrule instance) {
		log.debug("attaching dirty RefTermSimrule instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefTermSimrule instance) {
		log.debug("attaching clean RefTermSimrule instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefTermSimruleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefTermSimruleDAO) ctx.getBean("RefTermSimruleDAO");
	}
}