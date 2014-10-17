package com.sosgps.wzt.orm;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * TModule entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.sosgps.wzt.orm.TModule
 * @author MyEclipse Persistence Tools
 */

public class TModuleDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(TModuleDAO.class);
	// property constants
	public static final String MODULE_NAME = "moduleName";
	public static final String MODULE_NAME_EN = "moduleNameEn";
	public static final String MODULE_CODE = "moduleCode";
	public static final String MODULE_TYPE = "moduleType";
	public static final String MODULE_PATH = "modulePath";
	public static final String MOUDLE_DESC = "moudleDesc";
	public static final String USAGE_FLAG = "usageFlag";
	public static final String CREATE_BY = "createBy";
	public static final String CREATE_DATE = "createDate";
	public static final String MODULE_ID = "moduleId";
	public static final String MODULE_GRADE = "moduleGrade";
	public static final String PARENTID = "parentid";
	public static final String LEAF_FLAG = "leafFlag";
	public static final String VISIBLE_FLAG = "visibleFlag";
	public static final String ENABLE_FLAG = "enableFlag";
	public static final String TREE_LEVEL = "treeLevel";
	public static final String SORTED_INDEX = "sortedIndex";

	protected void initDao() {
		// do nothing
	}

	public boolean save(TModule transientInstance) {
		log.debug("saving TModule instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
			return true;
		} catch (RuntimeException re) {
			log.error("save failed", re);
			return false;
		}
	}

	public void update(TModule transientInstance) {
		log.debug("update TModule instance");
		try {
			getHibernateTemplate().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	public void delete(TModule persistentInstance) {
		log.debug("deleting TModule instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TModule findById(java.lang.Long id) {
		log.debug("getting TModule instance with id: " + id);
		try {
			TModule instance = (TModule) getHibernateTemplate().get(
					"com.sosgps.wzt.orm.TModule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TModule instance) {
		log.debug("finding TModule instance by example");
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
		log.debug("finding TModule instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TModule as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByModuleName(Object moduleName) {
		return findByProperty(MODULE_NAME, moduleName);
	}

	public List findByModuleCode(Object moduleCode) {
		return findByProperty(MODULE_CODE, moduleCode);
	}

	public List findByModuleType(Object moduleType) {
		return findByProperty(MODULE_TYPE, moduleType);
	}

	public List findByModulePath(Object modulePath) {
		return findByProperty(MODULE_PATH, modulePath);
	}

	public List findByMoudleDesc(Object moudleDesc) {
		return findByProperty(MOUDLE_DESC, moudleDesc);
	}

	public List findByUsageFlag(Object usageFlag) {
		return findByProperty(USAGE_FLAG, usageFlag);
	}

	public List findByCreateBy(Object createBy) {
		return findByProperty(CREATE_BY, createBy);
	}

	public List findByCreateDate(Object createDate) {
		return findByProperty(CREATE_DATE, createDate);
	}

	public List findByModuleId(Object moduleId) {
		return findByProperty(MODULE_ID, moduleId);
	}

	public List findByModuleGrade(Object moduleGrade) {
		return findByProperty(MODULE_GRADE, moduleGrade);
	}

	public List findByParentid(Object parentid) {
		return findByProperty(PARENTID, parentid);
	}

	public List findByLeafFlag(Object leafFlag) {
		return findByProperty(LEAF_FLAG, leafFlag);
	}

	public List findByVisibleFlag(Object visibleFlag) {
		return findByProperty(VISIBLE_FLAG, visibleFlag);
	}

	public List findByEnableFlag(Object enableFlag) {
		return findByProperty(ENABLE_FLAG, enableFlag);
	}

	public List findByTreeLevel(Object treeLevel) {
		return findByProperty(TREE_LEVEL, treeLevel);
	}

	public List findBySortedIndex(Object sortedIndex) {
		return findByProperty(SORTED_INDEX, sortedIndex);
	}

	public List findAll() {
		log.debug("finding all TModule instances");
		try {
			String queryString = "from TModule where sortedIndex is not null";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TModule merge(TModule detachedInstance) {
		log.debug("merging TModule instance");
		try {
			TModule result = (TModule) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TModule instance) {
		log.debug("attaching dirty TModule instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TModule instance) {
		log.debug("attaching clean TModule instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TModuleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TModuleDAO) ctx.getBean("TModuleDAO");
	}
}