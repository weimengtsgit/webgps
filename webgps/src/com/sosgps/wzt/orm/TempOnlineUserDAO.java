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
 * TempOnlineUser entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TempOnlineUser
 * @author MyEclipse Persistence Tools
 */

public class TempOnlineUserDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TempOnlineUserDAO.class);
	// property constants
	public static final String USER_ID = "userId";
	public static final String SESSIONID = "sessionid";
	public static final String EMP_CODE = "empCode";

	protected void initDao() {
		// do nothing
	}

	public void save(TempOnlineUser transientInstance) {
		log.debug("saving TempOnlineUser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TempOnlineUser persistentInstance) {
		log.debug("deleting TempOnlineUser instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TempOnlineUser findById(java.lang.Long id) {
		log.debug("getting TempOnlineUser instance with id: " + id);
		try {
			TempOnlineUser instance = (TempOnlineUser) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TempOnlineUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TempOnlineUser instance) {
		log.debug("finding TempOnlineUser instance by example");
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
		log.debug("finding TempOnlineUser instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TempOnlineUser as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findBySessionid(Object sessionid) {
		return findByProperty(SESSIONID, sessionid);
	}

	public List findByEmpCode(Object empCode) {
		return findByProperty(EMP_CODE, empCode);
	}

	public List findAll() {
		log.debug("finding all TempOnlineUser instances");
		try {
			String queryString = "from TempOnlineUser";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TempOnlineUser merge(TempOnlineUser detachedInstance) {
		log.debug("merging TempOnlineUser instance");
		try {
			TempOnlineUser result = (TempOnlineUser) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TempOnlineUser instance) {
		log.debug("attaching dirty TempOnlineUser instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TempOnlineUser instance) {
		log.debug("attaching clean TempOnlineUser instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TempOnlineUserDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TempOnlineUserDAO) ctx.getBean("TempOnlineUserDAO");
	}
}