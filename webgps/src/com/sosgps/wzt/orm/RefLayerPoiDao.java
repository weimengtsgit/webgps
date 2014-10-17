package com.sosgps.wzt.orm;

// Generated 2010-4-10 16:44:48 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class RefLayerPoi.
 * @see com.sosgps.wzt.orm.RefLayerPoi
 * @author Hibernate Tools
 */

public class RefLayerPoiDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(RefLayerPoiDao.class);

	//property constants
	public static final String ID = "id";

	public static final String TPOI = "TPoi";

	public static final String TLAYERS = "TLayers";

	public List findByTPoi(Object TPoi) {
		return findByProperty(TPOI, TPoi);
	}

	public List findByTLayers(Object TLayers) {
		return findByProperty(TLAYERS, TLayers);
	}

	public void save(RefLayerPoi transientInstance) {
		log.debug("saving RefLayerPoi instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RefLayerPoi persistentInstance) {
		log.debug("deleting RefLayerPoi instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding RefLayerPoi instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from RefLayerPoi as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public RefLayerPoi findById(java.lang.Long id) {
		log.debug("getting RefLayerPoi instance with id: " + id);
		try {
			RefLayerPoi instance = (RefLayerPoi) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.RefLayerPoi", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RefLayerPoi instance) {
		log.debug("finding RefLayerPoi instance by example");
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
		log.debug("finding all RefLayerPoi instances");
		try {
			String queryString = "from RefLayerPoi";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RefLayerPoi merge(RefLayerPoi detachedInstance) {
		log.debug("merging RefLayerPoi instance");
		try {
			RefLayerPoi result = (RefLayerPoi) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RefLayerPoi instance) {
		log.debug("attaching dirty RefLayerPoi instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RefLayerPoi instance) {
		log.debug("attaching clean RefLayerPoi instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static RefLayerPoiDao getFromApplicationContext(
			ApplicationContext ctx) {
		return (RefLayerPoiDao) ctx.getBean("RefLayerPoiHome");
	}
}
