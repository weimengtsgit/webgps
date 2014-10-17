package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TAttendanceLocrecord entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TAttendanceLocrecord
 * @author MyEclipse Persistence Tools
 */

public class TAttendanceLocrecordDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(TAttendanceLocrecordDAO.class);
	// property constants
	public static final String DEVICE_ID = "deviceId";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String ENT_CODE = "entCode";
	public static final String LOC_DESC = "locDesc";
	public static final String ATTENDANCE_RESULT = "attendanceResult";
	public static final String LOC_ID = "locId";
	public static final String JMX = "jmx";
	public static final String JMY = "jmy";

	protected void initDao() {
		// do nothing
	}

	public void save(TAttendanceLocrecord transientInstance) {
		log.debug("saving TAttendanceLocrecord instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TAttendanceLocrecord persistentInstance) {
		log.debug("deleting TAttendanceLocrecord instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TAttendanceLocrecord findById(java.lang.Long id) {
		log.debug("getting TAttendanceLocrecord instance with id: " + id);
		try {
			TAttendanceLocrecord instance = (TAttendanceLocrecord) getHibernateTemplate()
					.get("com.sosgps.wzt.orm.TAttendanceLocrecord", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TAttendanceLocrecord instance) {
		log.debug("finding TAttendanceLocrecord instance by example");
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
		log.debug("finding TAttendanceLocrecord instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TAttendanceLocrecord as model where model."
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

	public List findByX(Object x) {
		return findByProperty(X, x);
	}

	public List findByY(Object y) {
		return findByProperty(Y, y);
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}

	public List findByLocDesc(Object locDesc) {
		return findByProperty(LOC_DESC, locDesc);
	}

	public List findByAttendanceResult(Object attendanceResult) {
		return findByProperty(ATTENDANCE_RESULT, attendanceResult);
	}

	public List findByLocId(Object locId) {
		return findByProperty(LOC_ID, locId);
	}

	public List findByJmx(Object jmx) {
		return findByProperty(JMX, jmx);
	}

	public List findByJmy(Object jmy) {
		return findByProperty(JMY, jmy);
	}

	public List findAll() {
		log.debug("finding all TAttendanceLocrecord instances");
		try {
			String queryString = "from TAttendanceLocrecord";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TAttendanceLocrecord merge(TAttendanceLocrecord detachedInstance) {
		log.debug("merging TAttendanceLocrecord instance");
		try {
			TAttendanceLocrecord result = (TAttendanceLocrecord) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TAttendanceLocrecord instance) {
		log.debug("attaching dirty TAttendanceLocrecord instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TAttendanceLocrecord instance) {
		log.debug("attaching clean TAttendanceLocrecord instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TAttendanceLocrecordDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TAttendanceLocrecordDAO) ctx.getBean("TAttendanceLocrecordDAO");
	}
}