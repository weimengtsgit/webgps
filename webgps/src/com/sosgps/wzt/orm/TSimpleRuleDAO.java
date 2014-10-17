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
 * TSimpleRule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TSimpleRule
 * @author MyEclipse Persistence Tools
 */

public class TSimpleRuleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TSimpleRuleDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String RULE_TYPE = "ruleType";
	public static final String EFFECT_PERIOD = "effectPeriod";
	public static final String RULE_CONTENT = "ruleContent";
	public static final String INTERVAL = "interval";
	public static final String ENT_CODE = "entCode";
	public static final String RULE_DESC = "ruleDesc";

	protected void initDao() {
		// do nothing
	}

	public void save(TSimpleRule transientInstance) {
		log.debug("saving TSimpleRule instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TSimpleRule persistentInstance) {
		log.debug("deleting TSimpleRule instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TSimpleRule findById(java.lang.Long id) {
		log.debug("getting TSimpleRule instance with id: " + id);
		try {
			TSimpleRule instance = (TSimpleRule) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TSimpleRule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TSimpleRule instance) {
		log.debug("finding TSimpleRule instance by example");
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
		log.debug("finding TSimpleRule instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TSimpleRule as model where model."
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

	public List findByRuleType(Object ruleType) {
		return findByProperty(RULE_TYPE, ruleType);
	}

	public List findByEffectPeriod(Object effectPeriod) {
		return findByProperty(EFFECT_PERIOD, effectPeriod);
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
		log.debug("finding all TSimpleRule instances");
		try {
			String queryString = "from TSimpleRule";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TSimpleRule merge(TSimpleRule detachedInstance) {
		log.debug("merging TSimpleRule instance");
		try {
			TSimpleRule result = (TSimpleRule) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TSimpleRule instance) {
		log.debug("attaching dirty TSimpleRule instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TSimpleRule instance) {
		log.debug("attaching clean TSimpleRule instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TSimpleRuleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TSimpleRuleDAO) ctx.getBean("TSimpleRuleDAO");
	}
}