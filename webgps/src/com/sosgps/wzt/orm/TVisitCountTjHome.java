package com.sosgps.wzt.orm;

// Generated 2010-4-24 20:08:52 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TVisitCountTj.
 * @see com.sosgps.wzt.orm.TVisitCountTj
 * @author Hibernate Tools
 */

public class TVisitCountTjHome extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TVisitCountTjHome.class);

	//property constants
	public static final String ID = "id";

	public static final String DEVICEID = "deviceId";

	public static final String VEHICLENUMBER = "vehicleNumber";

	public static final String VISITCOUNT = "visitCount";
	
	public static final String VISITCUSCOUNT = "visitCusCount";
	
	public static final String VISITPLACECOUNT = "visitPlaceCount";

	public static final String CRTDATE = "crtdate";
	
	public static final String TJDATE = "tjDate";

	@SuppressWarnings("rawtypes")
	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICEID, deviceId);
	}
	@SuppressWarnings("rawtypes")
	public List findByVehicleNumber(Object vehicleNumber) {
		return findByProperty(VEHICLENUMBER, vehicleNumber);
	}
	@SuppressWarnings("rawtypes")
	public List findByVisitCount(Object visitCount) {
		return findByProperty(VISITCOUNT, visitCount);
	}
	@SuppressWarnings("rawtypes")
	public List findByVisitCusCount(Object visitCusCount) {
		return findByProperty(VISITCUSCOUNT, visitCusCount);
	}
	@SuppressWarnings("rawtypes")
	public List findByVisitPlaceCount(Object visitPlaceCount) {
		return findByProperty(VISITPLACECOUNT, visitPlaceCount);
	}
	@SuppressWarnings("rawtypes")
	public List findByTjDate(Object tjDate) {
		return findByProperty(TJDATE, tjDate);
	}
	@SuppressWarnings("rawtypes")
	public List findByCrtdate(Object crtdate) {
		return findByProperty(CRTDATE, crtdate);
	}

	public void save(TVisitCountTj transientInstance) {
		log.debug("saving TVisitCountTj instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TVisitCountTj persistentInstance) {
		log.debug("deleting TVisitCountTj instance");
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
		log.debug("finding TVisitCountTj instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TVisitCountTj as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TVisitCountTj findById(java.lang.Long id) {
		log.debug("getting TVisitCountTj instance with id: " + id);
		try {
			TVisitCountTj instance = (TVisitCountTj) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TVisitCountTj", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	@SuppressWarnings("rawtypes")
	public List findByExample(TVisitCountTj instance) {
		log.debug("finding TVisitCountTj instance by example");
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
		log.debug("finding all TVisitCountTj instances");
		try {
			String queryString = "from TVisitCountTj";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TVisitCountTj merge(TVisitCountTj detachedInstance) {
		log.debug("merging TVisitCountTj instance");
		try {
			TVisitCountTj result = (TVisitCountTj) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TVisitCountTj instance) {
		log.debug("attaching dirty TVisitCountTj instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TVisitCountTj instance) {
		log.debug("attaching clean TVisitCountTj instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TVisitCountTjHome getFromApplicationContext(ApplicationContext ctx) {
		return (TVisitCountTjHome) ctx.getBean("TVisitCountTjHome");
	}
}
