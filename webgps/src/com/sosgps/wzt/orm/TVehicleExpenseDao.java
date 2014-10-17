package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TVehicleExpenseDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TVehicleExpenseDao.class);

	//property constants
	public static final String ID = "id";
	public static final String DEVICEID = "deviceId";
	public static final String VEHICLEDEPRECIATION = "vehicleDepreciation";
	public static final String PERSONALEXPENSES = "personalExpenses";
	public static final String INSURANCE = "insurance";
	public static final String MAINTENANCE = "maintenance";
	public static final String TOLL = "toll";
	public static final String ANNUALCHECK = "annualCheck";
	public static final String CREATEDATE = "createDate";
	public static final String CHANGEDATE = "changeDate";
	public static final String EMPCODE = "empCode";
	public static final String USERID = "userId";

	public void save(TVehicleExpense transientInstance) {
		log.debug("saving TVehicleExpense instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TVehicleExpense persistentInstance) {
		log.debug("deleting TVehicleExpense instance");
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
		log.debug("finding TVehicleExpense instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TVehicleExpense as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TVehicleExpense findById(java.lang.Long id) {
		log.debug("getting TVehicleExpense instance with id: " + id);
		try {
			TVehicleExpense instance = (TVehicleExpense) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVehicleExpense", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TVehicleExpense instance) {
		log.debug("finding TVehicleExpense instance by example");
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
		log.debug("finding all TVehicleExpense instances");
		try {
			String queryString = "from TVehicleExpense";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVehicleExpense merge(TVehicleExpense detachedInstance) {
		log.debug("merging TVehicleExpense instance");
		try {
			TVehicleExpense result = (TVehicleExpense) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVehicleExpense instance) {
		log.debug("attaching dirty TVehicleExpense instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVehicleExpense instance) {
		log.debug("attaching clean TVehicleExpense instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void update(TVehicleExpense transientInstance){
        log.debug("updating TVehicleExpense instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
	public static TVehicleExpenseDao getFromApplicationContext(ApplicationContext ctx) {
		return (TVehicleExpenseDao) ctx.getBean("TVehicleExpense");
	}
}
