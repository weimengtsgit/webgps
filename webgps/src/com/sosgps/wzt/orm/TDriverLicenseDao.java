package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TDriverLicenseDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TDriverLicenseDao.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String EMPCODE = "empCode";

	public static final String USERID = "userId";

	public static final String CREATEDATE = "createDate";

	public static final String CHANGEDATE = "changeDate";

	public void save(TDriverLicense transientInstance) {
		log.debug("saving TDriverLicense instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TDriverLicense persistentInstance) {
		log.debug("deleting TDriverLicense instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TDriverLicense instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TDriverLicense as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TDriverLicense findById(java.lang.Long id) {
		log.debug("getting TDriverLicense instance with id: " + id);
		try {
			TDriverLicense instance = (TDriverLicense) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TDriverLicense", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TDriverLicense instance) {
		log.debug("finding TDriverLicense instance by example");
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

	@SuppressWarnings("rawtypes")
	public List findAll() {
		log.debug("finding all TDriverLicense instances");
		try {
			String queryString = "from TDriverLicense";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TDriverLicense merge(TDriverLicense detachedInstance) {
		log.debug("merging TDriverLicense instance");
		try {
			TDriverLicense result = (TDriverLicense) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TDriverLicense instance) {
		log.debug("attaching dirty TDriverLicense instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TDriverLicense instance) {
		log.debug("attaching clean TDriverLicense instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void update(TDriverLicense transientInstance){
        log.debug("updating TDriverLicense instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
	public static TDriverLicenseDao getFromApplicationContext(ApplicationContext ctx) {
		return (TDriverLicenseDao) ctx.getBean("TDriverLicense");
	}
}
