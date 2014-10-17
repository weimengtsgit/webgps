package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TInsuranceDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TInsuranceDao.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String EMPCODE = "empCode";

	public static final String USERID = "userId";

	public static final String CREATEDATE = "createDate";

	public static final String CHANGEDATE = "changeDate";

	public void save(TInsurance transientInstance) {
		log.debug("saving TInsurance instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TInsurance persistentInstance) {
		log.debug("deleting TInsurance instance");
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
		log.debug("finding TInsurance instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TInsurance as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TInsurance findById(java.lang.Long id) {
		log.debug("getting TInsurance instance with id: " + id);
		try {
			TInsurance instance = (TInsurance) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TInsurance", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TInsurance instance) {
		log.debug("finding TInsurance instance by example");
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
		log.debug("finding all TInsurance instances");
		try {
			String queryString = "from TInsurance";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TInsurance merge(TInsurance detachedInstance) {
		log.debug("merging TInsurance instance");
		try {
			TInsurance result = (TInsurance) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TInsurance instance) {
		log.debug("attaching dirty TInsurance instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TInsurance instance) {
		log.debug("attaching clean TInsurance instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void update(TInsurance transientInstance){
        log.debug("updating TInsurance instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
	public static TInsuranceDao getFromApplicationContext(ApplicationContext ctx) {
		return (TInsuranceDao) ctx.getBean("TInsurance");
	}
}
