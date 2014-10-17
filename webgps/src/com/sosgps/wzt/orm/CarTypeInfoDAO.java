package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class CarTypeInfo.
 * 
 * @see com.sosgps.wzt.orm.CarTypeInfo
 * @author MyEclipse Persistence Tools
 */

public class CarTypeInfoDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(CarTypeInfoDAO.class);

	// property constants
	public static final String TYPE_NAME = "typeName";

	public static final String ICON_PATH = "iconPath";

	public static final String DESCTION = "desction";

	public static final String EN_CODE = "enCode";

	public static final String OIL_WEAR = "oilWear";

	protected void initDao() {
		// do nothing
	}

	public void save(CarTypeInfo transientInstance) {
		log.debug("saving CarTypeInfo instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CarTypeInfo persistentInstance) {
		log.debug("deleting CarTypeInfo instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CarTypeInfo findById(java.lang.Long id) {
		log.debug("getting CarTypeInfo instance with id: " + id);
		try {
			CarTypeInfo instance = (CarTypeInfo) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.CarTypeInfo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CarTypeInfo instance) {
		log.debug("finding CarTypeInfo instance by example");
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
		log.debug("finding CarTypeInfo instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from CarTypeInfo as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTypeName(Object typeName) {
		return findByProperty(TYPE_NAME, typeName);
	}

	public List findByIconPath(Object iconPath) {
		return findByProperty(ICON_PATH, iconPath);
	}

	public List findByDesction(Object desction) {
		return findByProperty(DESCTION, desction);
	}

	public List findByEnCode(Object enCode) {
		return findByProperty(EN_CODE, enCode);
	}

	public List findAll() {
		log.debug("finding all CarTypeInfo instances");
		try {
			String queryString = "from CarTypeInfo";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CarTypeInfo merge(CarTypeInfo detachedInstance) {
		log.debug("merging CarTypeInfo instance");
		try {
			CarTypeInfo result = (CarTypeInfo) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CarTypeInfo instance) {
		log.debug("attaching dirty CarTypeInfo instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CarTypeInfo instance) {
		log.debug("attaching clean CarTypeInfo instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CarTypeInfoDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (CarTypeInfoDAO) ctx.getBean("CarTypeInfoDAO");
	}
	
	public void update(CarTypeInfo transientInstance){
        log.debug("updating CarTypeInfoDAO instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
}