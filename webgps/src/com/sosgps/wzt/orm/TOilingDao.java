package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TOilingDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TOilingDao.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String OILLITER = "oilLiter";

	public static final String OILCOST = "oilCost";

	public static final String EMPCODE = "empCode";

	public static final String USERID = "userId";

	public static final String CREATEDATE = "createDate";

	public static final String CHANGEDATE = "changeDate";

	public void save(TOiling transientInstance) {
		log.debug("saving TOiling instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TOiling persistentInstance) {
		log.debug("deleting TOiling instance");
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
		log.debug("finding TOiling instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TOiling as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TOiling findById(java.lang.Long id) {
		log.debug("getting TOiling instance with id: " + id);
		try {
			TOiling instance = (TOiling) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TOiling", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TOiling instance) {
		log.debug("finding TOiling instance by example");
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
		log.debug("finding all TOiling instances");
		try {
			String queryString = "from TOiling";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TOiling merge(TOiling detachedInstance) {
		log.debug("merging TOiling instance");
		try {
			TOiling result = (TOiling) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TOiling instance) {
		log.debug("attaching dirty TOiling instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TOiling instance) {
		log.debug("attaching clean TOiling instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void update(TOiling transientInstance){
        log.debug("updating TOiling instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
	public static TOilingDao getFromApplicationContext(ApplicationContext ctx) {
		return (TOilingDao) ctx.getBean("TOiling");
	}
}
