package com.sosgps.wzt.orm;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SmsRechargeAccountsDao extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SmsRechargeAccountsDao.class);

	// property constants
	public static final String ENT_CODE = "entCode";
	public static final String SMS_AVAILABLE = "smsAvailable";
	public static final String SMS_SENT_COUNT = "sms_sent_count";
	public static final String SMS_TOTAL = "sms_total";

	protected void initDao() {
		// do nothing
	}

	public void save(SmsAccounts transientInstance) {
		log.debug("saving SmsAccounts instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SmsAccounts persistentInstance) {
		log.debug("deleting SmsAccounts instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void update(SmsAccounts persistentInstance) {
		log.debug("update SmsAccounts instance");
		try {
			getHibernateTemplate().update(persistentInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public SmsAccounts findById(java.lang.String id) {
		log.debug("getting SmsAccounts instance with id: " + id);
		try {
			SmsAccounts instance = (SmsAccounts) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.SmsAccounts", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SmsAccounts instance) {
		log.debug("finding SmsAccounts instance by example");
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
		log.debug("finding TEnt instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from SmsAccounts as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public void attachDirty(SmsAccounts instance) {
		log.debug("attaching dirty SmsAccounts instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SmsAccounts instance) {
		log.debug("attaching clean TEnt instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}
