package com.sosgps.wzt.orm;

// Generated 2010-4-10 16:44:48 by Hibernate Tools 3.2.5.Beta

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Home object for domain model class TPoi.
 * @see com.sosgps.wzt.orm.TPoi
 * @author Hibernate Tools
 */

public class TPoiDao extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(TPoiDao.class);

	//property constants
	public static final String ID = "id";

	public static final String POINAME = "poiName";

	public static final String POIDESC = "poiDesc";

	public static final String POITYPE = "poiType";

	public static final String CREATOR = "creator";

	public static final String CDATE = "cdate";

	public static final String POIDATAS = "poiDatas";

	public static final String TELEPHONE = "telephone";

	public static final String ADDRESS = "address";

	public static final String ENTCODE = "entcode";

	public static final String KEYWORD = "keyword";

	public static final String POIENCRYPTDATAS = "poiEncryptDatas";

	public static final String ICONPATH = "iconpath";

	public static final String BORDERLINEWIDTH = "borderLineWidth";

	public static final String BORDERLINECOLOR = "borderLineColor";

	public static final String BORDERLINEALPHA = "borderLineAlpha";

	public static final String FILLCOLOR = "fillColor";

	public static final String FILLALPHA = "fillAlpha";

	public static final String VISIBLE = "visible";

	public static final String REFLAYERPOIS = "refLayerPois";

	public static final String REFTERMPOIS = "refTermPois";

	public List findByPoiName(Object poiName) {
		return findByProperty(POINAME, poiName);
	}

	public List findByPoiDesc(Object poiDesc) {
		return findByProperty(POIDESC, poiDesc);
	}

	public List findByPoiType(Object poiType) {
		return findByProperty(POITYPE, poiType);
	}

	public List findByCreator(Object creator) {
		return findByProperty(CREATOR, creator);
	}

	public List findByCdate(Object cdate) {
		return findByProperty(CDATE, cdate);
	}

	public List findByPoiDatas(Object poiDatas) {
		return findByProperty(POIDATAS, poiDatas);
	}

	public List findByTelephone(Object telephone) {
		return findByProperty(TELEPHONE, telephone);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByEntcode(Object entcode) {
		return findByProperty(ENTCODE, entcode);
	}

	public List findByKeyword(Object keyword) {
		return findByProperty(KEYWORD, keyword);
	}

	public List findByPoiEncryptDatas(Object poiEncryptDatas) {
		return findByProperty(POIENCRYPTDATAS, poiEncryptDatas);
	}

	public List findByIconpath(Object iconpath) {
		return findByProperty(ICONPATH, iconpath);
	}

	public List findByBorderLineWidth(Object borderLineWidth) {
		return findByProperty(BORDERLINEWIDTH, borderLineWidth);
	}

	public List findByBorderLineColor(Object borderLineColor) {
		return findByProperty(BORDERLINECOLOR, borderLineColor);
	}

	public List findByBorderLineAlpha(Object borderLineAlpha) {
		return findByProperty(BORDERLINEALPHA, borderLineAlpha);
	}

	public List findByFillColor(Object fillColor) {
		return findByProperty(FILLCOLOR, fillColor);
	}

	public List findByFillAlpha(Object fillAlpha) {
		return findByProperty(FILLALPHA, fillAlpha);
	}

	public List findByVisible(Object visible) {
		return findByProperty(VISIBLE, visible);
	}

	public List findByRefLayerPois(Object refLayerPois) {
		return findByProperty(REFLAYERPOIS, refLayerPois);
	}

	public List findByRefTermPois(Object refTermPois) {
		return findByProperty(REFTERMPOIS, refTermPois);
	}

	public void save(TPoi transientInstance) {
		log.debug("saving TPoi instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TPoi persistentInstance) {
		log.debug("deleting TPoi instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding TPoi instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TPoi as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public TPoi findById(java.lang.Long id) {
		log.debug("getting TPoi instance with id: " + id);
		try {
			TPoi instance = (TPoi) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TPoi", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TPoi instance) {
		log.debug("finding TPoi instance by example");
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
		log.debug("finding all TPoi instances");
		try {
			String queryString = "from TPoi";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TPoi merge(TPoi detachedInstance) {
		log.debug("merging TPoi instance");
		try {
			TPoi result = (TPoi) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TPoi instance) {
		log.debug("attaching dirty TPoi instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TPoi instance) {
		log.debug("attaching clean TPoi instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TPoiDao getFromApplicationContext(ApplicationContext ctx) {
		return (TPoiDao) ctx.getBean("TPoiHome");
	}
}
