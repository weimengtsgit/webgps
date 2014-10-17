package com.sosgps.wzt.orm;

// Generated 2010-4-10 16:44:48 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TLayers.
 * @see com.sosgps.wzt.orm.TLayers
 * @author Hibernate Tools
 */

public class TLayersDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TLayersDao.class);

	//property constants
	public static final String ID = "id";

	public static final String LAYERNAME = "layerName";

	public static final String LAYERDESC = "layerDesc";

	public static final String USESTATUS = "useStatus";

	public static final String CTDATE = "ctdate";

	public static final String ENTCODE = "entcode";

	public static final String USERID = "userId";

	public static final String INFO1 = "info1";

	public static final String INFO2 = "info2";

	public static final String VISIBLE = "visible";

	public static final String REFUSERLAYERS = "refUserLayers";

	public static final String REFLAYERPOIS = "refLayerPois";

	public List findByLayerName(Object layerName) {
		return findByProperty(LAYERNAME, layerName);
	}

	public List findByLayerDesc(Object layerDesc) {
		return findByProperty(LAYERDESC, layerDesc);
	}

	public List findByUseStatus(Object useStatus) {
		return findByProperty(USESTATUS, useStatus);
	}

	public List findByCtdate(Object ctdate) {
		return findByProperty(CTDATE, ctdate);
	}

	public List findByEntcode(Object entcode) {
		return findByProperty(ENTCODE, entcode);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USERID, userId);
	}

	public List findByInfo1(Object info1) {
		return findByProperty(INFO1, info1);
	}

	public List findByInfo2(Object info2) {
		return findByProperty(INFO2, info2);
	}

	public List findByVisible(Object visible) {
		return findByProperty(VISIBLE, visible);
	}

	public List findByRefUserLayers(Object refUserLayers) {
		return findByProperty(REFUSERLAYERS, refUserLayers);
	}

	public List findByRefLayerPois(Object refLayerPois) {
		return findByProperty(REFLAYERPOIS, refLayerPois);
	}

	public void save(TLayers transientInstance) {
		log.debug("saving TLayers instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TLayers persistentInstance) {
		log.debug("deleting TLayers instance");
		try {
		    //getHibernateTemplate().delete(persistentInstance);
		    //»Ì…æ≥˝Õº≤„
		    persistentInstance.setStates(1L);
		    getHibernateTemplate().update(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TLayers instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TLayers as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TLayers findById(java.lang.Long id) {
		log.debug("getting TLayers instance with id: " + id);
		try {
			TLayers instance = (TLayers) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TLayers", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TLayers instance) {
		log.debug("finding TLayers instance by example");
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

	public List findAll() {
		log.debug("finding all TLayers instances");
		try {
			String queryString = "from TLayers";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TLayers merge(TLayers detachedInstance) {
		log.debug("merging TLayers instance");
		try {
			TLayers result = (TLayers) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TLayers instance) {
		log.debug("attaching dirty TLayers instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TLayers instance) {
		log.debug("attaching clean TLayers instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TLayersDao getFromApplicationContext(ApplicationContext ctx) {
		return (TLayersDao) ctx.getBean("TLayersHome");
	}
}
