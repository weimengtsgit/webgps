package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TAnnualExaminationDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TAnnualExaminationDao.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String EMPCODE = "empCode";

	public static final String USERID = "userId";

	public static final String CREATEDATE = "createDate";

	public static final String CHANGEDATE = "changeDate";

	public void save(TAnnualExamination transientInstance) {
		log.debug("saving TAnnualExamination instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TAnnualExamination persistentInstance) {
		log.debug("deleting TAnnualExamination instance");
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
		log.debug("finding TAnnualExamination instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TAnnualExamination as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TAnnualExamination findById(java.lang.Long id) {
		log.debug("getting TAnnualExamination instance with id: " + id);
		try {
			TAnnualExamination instance = (TAnnualExamination) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TAnnualExamination", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List findByExample(TAnnualExamination instance) {
		log.debug("finding TAnnualExamination instance by example");
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
		log.debug("finding all TAnnualExamination instances");
		try {
			String queryString = "from TAnnualExamination";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAnnualExamination merge(TAnnualExamination detachedInstance) {
		log.debug("merging TAnnualExamination instance");
		try {
			TAnnualExamination result = (TAnnualExamination) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAnnualExamination instance) {
		log.debug("attaching dirty TAnnualExamination instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAnnualExamination instance) {
		log.debug("attaching clean TAnnualExamination instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public void update(TAnnualExamination transientInstance){
        log.debug("updating TAnnualExamination instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("saupdateve failed", re);
            throw re;
        }
    }
	
	public static TAnnualExaminationDao getFromApplicationContext(ApplicationContext ctx) {
		return (TAnnualExaminationDao) ctx.getBean("TAnnualExamination");
	}
}
