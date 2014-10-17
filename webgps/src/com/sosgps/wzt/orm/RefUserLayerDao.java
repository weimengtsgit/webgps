package com.sosgps.wzt.orm;

// Generated 2010-4-10 16:44:48 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class RefUserLayer.
 * @see com.sosgps.wzt.orm.RefUserLayer
 * @author Hibernate Tools
 */

public class RefUserLayerDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(RefUserLayerDao.class);

	//property constants
	public static final String ID = "id";

	public static final String TLAYERS = "TLayers";

	public static final String USERID = "userId";

	public static final String VISIBLE = "visible";

	public List findByTLayers(Object TLayers) {
		return findByProperty(TLAYERS, TLayers);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USERID, userId);
	}

	public List findByVisible(Object visible) {
		return findByProperty(VISIBLE, visible);
	}

	public void save(RefUserLayer transientInstance) {
		log.debug("saving RefUserLayer instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RefUserLayer persistentInstance) {
		log.debug("deleting RefUserLayer instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding RefUserLayer instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RefUserLayer as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public RefUserLayer findById(java.lang.Long id) {
		log.debug("getting RefUserLayer instance with id: " + id);
		try {
			RefUserLayer instance = (RefUserLayer) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.RefUserLayer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefUserLayer instance) {
		log.debug("finding RefUserLayer instance by example");
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
		log.debug("finding all RefUserLayer instances");
		try {
			String queryString = "from RefUserLayer";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefUserLayer merge(RefUserLayer detachedInstance) {
		log.debug("merging RefUserLayer instance");
		try {
			RefUserLayer result = (RefUserLayer) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefUserLayer instance) {
		log.debug("attaching dirty RefUserLayer instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefUserLayer instance) {
		log.debug("attaching clean RefUserLayer instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefUserLayerDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefUserLayerDao) ctx.getBean("RefUserLayerHome");
	}
}
