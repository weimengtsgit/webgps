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
 * TAttendanceRule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TAttendanceRule
 * @author MyEclipse Persistence Tools
 */

public class TAttendanceRuleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TAttendanceRuleDAO.class);
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

	public void save(TAttendanceRule transientInstance) {
		log.debug("saving TAttendanceRule instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TAttendanceRule persistentInstance) {
		log.debug("deleting TAttendanceRule instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TAttendanceRule findById(java.lang.Long id) {
		log.debug("getting TAttendanceRule instance with id: " + id);
		try {
			TAttendanceRule instance = (TAttendanceRule) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TAttendanceRule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TAttendanceRule instance) {
		log.debug("finding TAttendanceRule instance by example");
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
		log.debug("finding TAttendanceRule instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TAttendanceRule as model where model."
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
		log.debug("finding all TAttendanceRule instances");
		try {
			String queryString = "from TAttendanceRule";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAttendanceRule merge(TAttendanceRule detachedInstance) {
		log.debug("merging TAttendanceRule instance");
		try {
			TAttendanceRule result = (TAttendanceRule) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAttendanceRule instance) {
		log.debug("attaching dirty TAttendanceRule instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAttendanceRule instance) {
		log.debug("attaching clean TAttendanceRule instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TAttendanceRuleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TAttendanceRuleDAO) ctx.getBean("TAttendanceRuleDAO");
	}
}