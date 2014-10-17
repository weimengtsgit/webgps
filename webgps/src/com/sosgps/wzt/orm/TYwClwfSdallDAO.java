package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TYwClwfSdall.
 * 
 * @see com.sosgps.wzt.orm.TYwClwfSdall
 * @author MyEclipse Persistence Tools
 */

public class TYwClwfSdallDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TYwClwfSdallDAO.class);

	// property constants
	public static final String XH = "xh";

	public static final String HPZL = "hpzl";

	public static final String HPHM = "hphm";

	public static final String WFDZ = "wfdz";

	public static final String WFXW = "wfxw";

	public static final String CJJG = "cjjg";

	public static final String CLBJ = "clbj";

	public static final String JDCSYR = "jdcsyr";

	public static final String DH = "dh";

	protected void initDao() {
		// do nothing
	}

	public void save(TYwClwfSdall transientInstance) {
		log.debug("saving TYwClwfSdall instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TYwClwfSdall persistentInstance) {
		log.debug("deleting TYwClwfSdall instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TYwClwfSdall findById(java.lang.Long id) {
		log.debug("getting TYwClwfSdall instance with id: " + id);
		try {
			TYwClwfSdall instance = (TYwClwfSdall) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TYwClwfSdall", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TYwClwfSdall instance) {
		log.debug("finding TYwClwfSdall instance by example");
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
		log.debug("finding TYwClwfSdall instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TYwClwfSdall as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByXh(Object xh) {
		return findByProperty(XH, xh);
	}

	public List findByHpzl(Object hpzl) {
		return findByProperty(HPZL, hpzl);
	}

	public List findByHphm(Object hphm) {
		return findByProperty(HPHM, hphm);
	}

	public List findByWfdz(Object wfdz) {
		return findByProperty(WFDZ, wfdz);
	}

	public List findByWfxw(Object wfxw) {
		return findByProperty(WFXW, wfxw);
	}

	public List findByCjjg(Object cjjg) {
		return findByProperty(CJJG, cjjg);
	}

	public List findByClbj(Object clbj) {
		return findByProperty(CLBJ, clbj);
	}

	public List findByJdcsyr(Object jdcsyr) {
		return findByProperty(JDCSYR, jdcsyr);
	}

	public List findByDh(Object dh) {
		return findByProperty(DH, dh);
	}

	public List findAll() {
		log.debug("finding all TYwClwfSdall instances");
		try {
			String queryString = "from TYwClwfSdall";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TYwClwfSdall merge(TYwClwfSdall detachedInstance) {
		log.debug("merging TYwClwfSdall instance");
		try {
			TYwClwfSdall result = (TYwClwfSdall) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TYwClwfSdall instance) {
		log.debug("attaching dirty TYwClwfSdall instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TYwClwfSdall instance) {
		log.debug("attaching clean TYwClwfSdall instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TYwClwfSdallDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (TYwClwfSdallDAO) ctx.getBean("TYwClwfSdallDAO");
	}
}