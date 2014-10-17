package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TBossRole entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TBossRole
 * @author MyEclipse Persistence Tools
 */

public class TBossRoleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TBossRoleDAO.class);
	// property constants
	public static final String BOSS_USER_TYPE = "bossUserType";
	public static final String BOSS_USER_TYPE_DESC = "bossUserTypeDesc";
	public static final String ROLE_ID = "roleId";

	protected void initDao() {
		// do nothing
	}

	public void save(TBossRole transientInstance) {
		log.debug("saving TBossRole instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TBossRole persistentInstance) {
		log.debug("deleting TBossRole instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TBossRole findById(java.lang.Long id) {
		log.debug("getting TBossRole instance with id: " + id);
		try {
			TBossRole instance = (TBossRole) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TBossRole", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TBossRole instance) {
		log.debug("finding TBossRole instance by example");
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
		log.debug("finding TBossRole instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TBossRole as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBossUserType(Object bossUserType) {
		return findByProperty(BOSS_USER_TYPE, bossUserType);
	}

	public List findByBossUserTypeDesc(Object bossUserTypeDesc) {
		return findByProperty(BOSS_USER_TYPE_DESC, bossUserTypeDesc);
	}

	public List findByRoleId(Object roleId) {
		return findByProperty(ROLE_ID, roleId);
	}

	public List findAll() {
		log.debug("finding all TBossRole instances");
		try {
			String queryString = "from TBossRole";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TBossRole merge(TBossRole detachedInstance) {
		log.debug("merging TBossRole instance");
		try {
			TBossRole result = (TBossRole) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TBossRole instance) {
		log.debug("attaching dirty TBossRole instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TBossRole instance) {
		log.debug("attaching clean TBossRole instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TBossRoleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TBossRoleDAO) ctx.getBean("TBossRoleDAO");
	}
}