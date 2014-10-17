package com.sosgps.wzt.orm;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for TRole
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TRole
 * @author MyEclipse Persistence Tools
 */

public class TRoleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TRoleDAO.class);
	// property constants
	public static final String ROLE_NAME = "roleName";
	public static final String ROLE_CODE = "roleCode";
	public static final String ROLE_DESC = "roleDesc";
	public static final String USAGE_FLAG = "usageFlag";
	public static final String CREATE_BY = "createBy";
	public static final String CREATE_DATE = "createDate";
	public static final String EMP_CODE = "empCode";

	protected void initDao() {
		// do nothing
	}

	public void save(TRole transientInstance) {
		log.debug("saving TRole instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void update(TRole persistentInstance) {
		log.debug("update TRole instance");
		try {
			getHibernateTemplate().update(persistentInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	public void delete(TRole persistentInstance) {
		log.debug("deleting TRole instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TRole findById(java.lang.Long id) {
		log.debug("getting TRole instance with id: " + id);
		try {
			TRole instance = (TRole) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TRole", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TRole instance) {
		log.debug("finding TRole instance by example");
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
		log.debug("finding TRole instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TRole as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRoleName(Object roleName) {
		return findByProperty(ROLE_NAME, roleName);
	}

	public List findByRoleCode(Object roleCode) {
		return findByProperty(ROLE_CODE, roleCode);
	}

	public List findByRoleDesc(Object roleDesc) {
		return findByProperty(ROLE_DESC, roleDesc);
	}

	public List findByUsageFlag(Object usageFlag) {
		return findByProperty(USAGE_FLAG, usageFlag);
	}

	public List findByCreateBy(Object createBy) {
		return findByProperty(CREATE_BY, createBy);
	}

	public List findByCreateDate(Object createDate) {
		return findByProperty(CREATE_DATE, createDate);
	}

	public List findByEmpCode(Object empCode) {
		return findByProperty(EMP_CODE, empCode);
	}

	public List findAll() {
		log.debug("finding all TRole instances");
		try {
			String queryString = "from TRole";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TRole merge(TRole detachedInstance) {
		log.debug("merging TRole instance");
		try {
			TRole result = (TRole) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TRole instance) {
		log.debug("attaching dirty TRole instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TRole instance) {
		log.debug("attaching clean TRole instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TRoleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TRoleDAO) ctx.getBean("TRoleDAO");
	}
}