package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TAttendanceRuleWait entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TAttendanceRuleWait
 * @author MyEclipse Persistence Tools
 */

public class TAttendanceRuleWaitDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TAttendanceRuleWaitDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String EFFECT_PERIOD = "effectPeriod";
	public static final String RULE_TYPE = "ruleType";
	public static final String RULE_CONTENT = "ruleContent";
	public static final String INTERVAL = "interval";
	public static final String ENT_CODE = "entCode";
	public static final String RULE_DESC = "ruleDesc";

	protected void initDao() {
		// do nothing
	}

	public void save(TAttendanceRuleWait transientInstance) {
		log.debug("saving TAttendanceRuleWait instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TAttendanceRuleWait persistentInstance) {
		log.debug("deleting TAttendanceRuleWait instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TAttendanceRuleWait findById(java.lang.Long id) {
		log.debug("getting TAttendanceRuleWait instance with id: " + id);
		try {
			TAttendanceRuleWait instance = (TAttendanceRuleWait) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TAttendanceRuleWait", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TAttendanceRuleWait instance) {
		log.debug("finding TAttendanceRuleWait instance by example");
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
		log.debug("finding TAttendanceRuleWait instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TAttendanceRuleWait as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByEffectPeriod(Object effectPeriod) {
		return findByProperty(EFFECT_PERIOD, effectPeriod);
	}

	public List findByRuleType(Object ruleType) {
		return findByProperty(RULE_TYPE, ruleType);
	}

	public List findByRuleContent(Object ruleContent) {
		return findByProperty(RULE_CONTENT, ruleContent);
	}

	public List findByInterval(Object interval) {
		return findByProperty(INTERVAL, interval);
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public List findByRuleDesc(Object ruleDesc) {
		return findByProperty(RULE_DESC, ruleDesc);
	}

	public List findAll() {
		log.debug("finding all TAttendanceRuleWait instances");
		try {
			String queryString = "from TAttendanceRuleWait";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAttendanceRuleWait merge(TAttendanceRuleWait detachedInstance) {
		log.debug("merging TAttendanceRuleWait instance");
		try {
			TAttendanceRuleWait result = (TAttendanceRuleWait) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAttendanceRuleWait instance) {
		log.debug("attaching dirty TAttendanceRuleWait instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAttendanceRuleWait instance) {
		log.debug("attaching clean TAttendanceRuleWait instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TAttendanceRuleWaitDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TAttendanceRuleWaitDAO) ctx.getBean("TAttendanceRuleDAOWait");
	}
}