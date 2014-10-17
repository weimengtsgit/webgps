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

public class TDispatchMoneyDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TDispatchMoneyDAO.class);

	// property constants
	public static final String ID = "id";

	public static final String DEVICE_ID = "deviceId";
	public static final String DISPATCH_AMOUNT = "dispatch_amount";
	public static final String DISPATCHDATE = "dispatchdate";
	public static final String FRMHANDLER = "frmhandler";
	public static final String FRMDEMO = "frmdemo";
	public static final String USER_ID = "user_id";
	public static final String SAVE_DATE = "save_date";

	protected void initDao() {
		// do nothing
	}

	public void save( TDispatchMoney transientInstance) {
		log.debug("saving  TDispatchMoney instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete( TDispatchMoney persistentInstance) {
		log.debug("deleting  TDispatchMoney instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public  TDispatchMoney findById(java.lang.Integer id) {
		log.debug("getting TDispatchMoney instance with id: " + id);
		try {
			TDispatchMoney instance = (TDispatchMoney) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TDistanceDay", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TDispatchMoney instance) {
		log.debug("finding TDistanceDay instance by example");
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
		log.debug("finding TDispatchMoney instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TDispatchMoney as model where model."
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
		log.debug("finding all TDispatchMoney instances");
		try {
			String queryString = "from TDispatchMoney";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TDispatchMoney merge(TDispatchMoney detachedInstance) {
		log.debug("merging TDispatchMoney instance");
		try {
			TDispatchMoney result = (TDispatchMoney) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	public void update(TDispatchMoney transientInstance){
        log.debug("updating TDispatchMoney instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	public void attachDirty(TDispatchMoney instance) {
		log.debug("attaching dirty TDispatchMoney instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TDispatchMoney instance) {
		log.debug("attaching clean TDispatchMoney instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TDispatchMoneyDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TDispatchMoneyDAO) ctx.getBean("TDispatchMoneyDAO");
	}
}