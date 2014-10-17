package com.sosgps.wzt.orm;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TTermGroup.
 * 
 * @see com.sosgps.wzt.orm.TTermGroup
 * @author MyEclipse Persistence Tools
 */

public class TTermGroupDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TTermGroupDAO.class);

	// property constants
	public static final String ENT_CODE = "entCode";

	public static final String GROUP_NAME = "groupName";

	public static final String PARENT_ID = "parentId";

	public static final String GROUP_SORT = "groupSort";
	
	public static final String WEEK = "week";

	protected void initDao() {
		// do nothing
	}

	public void save(TTermGroup transientInstance) {
		log.debug("saving TTermGroup instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TTermGroup persistentInstance) {
		log.debug("deleting TTermGroup instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TTermGroup findById(java.lang.Long id) {
		log.debug("getting TTermGroup instance with id: " + id);
		try {
			TTermGroup instance = (TTermGroup) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TTermGroup", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TTermGroup instance) {
		log.debug("finding TTermGroup instance by example");
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
		log.debug("finding TTermGroup instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TTermGroup as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value, Object value2) {
		log.debug("finding TTermGroup instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TTermGroup as model where model."
					+ propertyName + "= ?"+" ORDER BY model."+value2+" desc";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public List findByGroupName(Object groupName) {
		return findByProperty(GROUP_NAME, groupName);
	}

	public List findByParent(Object parentId) {
		return findByProperty(PARENT_ID, parentId,this.GROUP_SORT);
	}

	public List findByGroupSort(Object groupSort) {
		return findByProperty(GROUP_SORT, groupSort);
	}

	public List findAll() {
		log.debug("finding all TTermGroup instances");
		try {
			String queryString = "from TTermGroup";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TTermGroup merge(TTermGroup detachedInstance) {
		log.debug("merging TTermGroup instance");
		try {
			TTermGroup result = (TTermGroup) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TTermGroup instance) {
		log.debug("attaching dirty TTermGroup instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TTermGroup instance) {
		log.debug("attaching clean TTermGroup instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TTermGroupDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TTermGroupDAO) ctx.getBean("TTermGroupDAO");
	}
}