package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TTerminal.
 * 
 * @see com.sosgps.wzt.orm.TTerminal
 * @author MyEclipse Persistence Tools
 */

public class TTerminalDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TTerminalDAO.class);

	// property constants
	public static final String TERM_NAME = "termName";

	public static final String IMG_URL = "imgUrl";

	public static final String SUITE_ID = "suiteId";

	public static final String ENT_CODE = "entCode";

	public static final String TYPE_CODE = "typeCode";

	public static final String SIMCARD = "simcard";

	public static final String OEM_CODE = "oemCode";

	public static final String DRIVER_NUMBER = "driverNumber";

	public static final String LOCATE_TYPE = "locateType";

	public static final String CAR_TYPE_ID = "carTypeId";

	public static final String IS_ALLOCATE = "isAllocate";

	public static final String SUB_COMPANY = "subCompany";
	
	public static final String WEEK = "week";
	
	//sxh
	public static final String IMSI = "imsi";

	protected void initDao() {
		// do nothing
	}

	public void save(TTerminal transientInstance) {
		log.debug("saving TTerminal instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TTerminal persistentInstance) {
		log.debug("deleting TTerminal instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TTerminal findById(java.lang.String id) {
		log.debug("getting TTerminal instance with id: " + id);
		try {
			TTerminal instance = (TTerminal) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TTerminal", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TTerminal instance) {
		log.debug("finding TTerminal instance by example");
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
		log.debug("finding TTerminal instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TTerminal as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTermName(Object termName) {
		return findByProperty(TERM_NAME, termName);
	}

	public List findByImgUrl(Object imgUrl) {
		return findByProperty(IMG_URL, imgUrl);
	}

	public List findBySuiteId(Object suiteId) {
		return findByProperty(SUITE_ID, suiteId);
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public List findByTypeCode(Object typeCode) {
		return findByProperty(TYPE_CODE, typeCode);
	}

	public List findBySimcard(Object simcard) {
		return findByProperty(SIMCARD, simcard);
	}

	public List findByOemCode(Object oemCode) {
		return findByProperty(OEM_CODE, oemCode);
	}

	public List findByDriverNumber(Object driverNumber) {
		return findByProperty(DRIVER_NUMBER, driverNumber);
	}

	public List findByLocateType(Object locateType) {
		return findByProperty(LOCATE_TYPE, locateType);
	}

	public List findByCarTypeId(Object carTypeId) {
		return findByProperty(CAR_TYPE_ID, carTypeId);
	}

	public List findByIsAllocate(Object isAllocate) {
		return findByProperty(IS_ALLOCATE, isAllocate);
	}

	public List findBySubCompany(Object subCompany) {
		return findByProperty(SUB_COMPANY, subCompany);
	}
	
	//sxh
	public List findByImsi(Object Imsi) {
		return findByProperty(IMSI, Imsi);
	}

	public List findAll() {
		log.debug("finding all TTerminal instances");
		try {
			String queryString = "from TTerminal";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TTerminal merge(TTerminal detachedInstance) {
		log.debug("merging TTerminal instance");
		try {
			TTerminal result = (TTerminal) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TTerminal instance) {
		log.debug("attaching dirty TTerminal instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TTerminal instance) {
		log.debug("attaching clean TTerminal instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TTerminalDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TTerminalDAO) ctx.getBean("TTerminalDAO");
	}
}