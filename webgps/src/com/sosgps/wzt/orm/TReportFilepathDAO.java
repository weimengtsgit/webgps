package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TReportFilepath entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sos.sosgps.wzt.orm.TReportFilepath
 * @author MyEclipse Persistence Tools
 */

public class TReportFilepathDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TReportFilepathDAO.class);
	// property constants
	public static final String DEVICE_ID = "deviceId";
	public static final String ENT_CODE = "entCode";
	public static final String FILE_PATH = "filePath";

	public void save(TReportFilepath transientInstance) {
        log.debug("saving TOptLog instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
	}

	public void delete(TReportFilepath persistentInstance) {
		log.debug("deleting TReportFilepath instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TReportFilepath findById(java.math.BigDecimal id) {
		log.debug("getting TReportFilepath instance with id: " + id);
		try {
			TReportFilepath instance = (TReportFilepath) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TReportFilepath", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TReportFilepath instance) {
		log.debug("finding TReportFilepath instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TReportFilepath instance with property: "
				+ propertyName + ", value: " + value);
	      try {
	          String queryString = "from TOptLog as model where model." 
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

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public List findByFilePath(Object filePath) {
		return findByProperty(FILE_PATH, filePath);
	}


	public TReportFilepath merge(TReportFilepath detachedInstance) {
		log.debug("merging TReportFilepath instance");
		try {
			TReportFilepath result = (TReportFilepath) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TReportFilepath instance) {
		log.debug("attaching dirty TReportFilepath instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TReportFilepath instance) {
		log.debug("attaching clean TReportFilepath instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}