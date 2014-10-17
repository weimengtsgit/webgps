package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TDistanceDay.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TVehicleNormDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TVehicleNormDAO.class);

	// property constants
	public static final String ID = "id";

	public static final String DEVICE_ID = "deviceId";
	public static final String MILEAGENORM = "mileagenorm";
	public static final String USER_ID = "user_id";
	public static final String SAVE_DATE = "save_date";

	protected void initDao() {
		// do nothing
	}

	public void save( TVehicleNorm transientInstance) {
		log.debug("saving  TVehicleNorm instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete( TVehicleNorm persistentInstance) {
		log.debug("deleting  TVehicleNorm instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public  TVehicleNorm findById(java.lang.Integer id) {
		log.debug("getting TVehicleNorm instance with id: " + id);
		try {
			TVehicleNorm instance = (TVehicleNorm) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVehicleNorm", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TVehicleNorm instance) {
		log.debug("finding TVehicleNorm instance by example");
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
		log.debug("finding TVehicleNorm instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TVehicleNorm as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICE_ID, deviceId);
	}

	

	public List findAll() {
		log.debug("finding all TVehicleNorm instances");
		try {
			String queryString = "from TVehicleNorm";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVehicleNorm merge(TVehicleNorm detachedInstance) {
		log.debug("merging TVehicleNorm instance");
		try {
			TVehicleNorm result = (TVehicleNorm) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVehicleNorm instance) {
		log.debug("attaching dirty TVehicleNorm instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVehicleNorm instance) {
		log.debug("attaching clean TVehicleNorm instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	public void update(TVehicleNorm transientInstance){
        log.debug("updating TVehicleNorm instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }

	public static TVehicleNormDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TVehicleNormDAO) ctx.getBean("TVehicleNormDAO");
	}
}