package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TVehiclesMaintenanceDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TVehiclesMaintenanceDao.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String EMPCODE = "empCode";

	public static final String USERID = "userId";

	public static final String CREATEDATE = "createDate";

	public static final String CHANGEDATE = "changeDate";

	public void save(TVehiclesMaintenance transientInstance) {
		log.debug("saving TVehiclesMaintenance instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TVehiclesMaintenance persistentInstance) {
		log.debug("deleting TVehiclesMaintenance instance");
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
		log.debug("finding TVehiclesMaintenance instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TVehiclesMaintenance as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TVehiclesMaintenance findById(java.lang.Long id) {
		log.debug("getting TVehiclesMaintenance instance with id: " + id);
		try {
			TVehiclesMaintenance instance = (TVehiclesMaintenance) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVehiclesMaintenance", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TVehiclesMaintenance instance) {
		log.debug("finding TVehiclesMaintenance instance by example");
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
		log.debug("finding all TVehiclesMaintenance instances");
		try {
			String queryString = "from TVehiclesMaintenance";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVehiclesMaintenance merge(TVehiclesMaintenance detachedInstance) {
		log.debug("merging TVehiclesMaintenance instance");
		try {
			TVehiclesMaintenance result = (TVehiclesMaintenance) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVehiclesMaintenance instance) {
		log.debug("attaching dirty TVehiclesMaintenance instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVehiclesMaintenance instance) {
		log.debug("attaching clean TVehiclesMaintenance instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void update(TVehiclesMaintenance transientInstance){
        log.debug("updating TVehiclesMaintenance instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
	public static TVehiclesMaintenanceDao getFromApplicationContext(ApplicationContext ctx) {
		return (TVehiclesMaintenanceDao) ctx.getBean("TVehiclesMaintenance");
	}
}
