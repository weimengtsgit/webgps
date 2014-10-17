package com.sosgps.wzt.orm;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TDict.
 * @see com.sosgps.wzt.orm.TDict
 * @author MyEclipse - Hibernate Tools
 */
public class TDictDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TDictDAO.class);

	//property constants
	public static final String DICT_NAME = "dictName";
	public static final String DICT_CODE = "dictCode";
	public static final String USAGE_FLAG = "usageFlag";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TDict transientInstance) {
        log.debug("saving TDict instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TDict persistentInstance) {
        log.debug("deleting TDict instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TDict findById( java.lang.Long id) {
        log.debug("getting TDict instance with id: " + id);
        try {
            TDict instance = (TDict) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TDict", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TDict instance) {
        log.debug("finding TDict instance by example");
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
      log.debug("finding TDict instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TDict as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByDictName(Object dictName) {
		return findByProperty(DICT_NAME, dictName);
	}
	
	public List findByDictCode(Object dictCode) {
		return findByProperty(DICT_CODE, dictCode);
	}
	
	public List findByUsageFlag(Object usageFlag) {
		return findByProperty(USAGE_FLAG, usageFlag);
	}
	
    public TDict merge(TDict detachedInstance) {
        log.debug("merging TDict instance");
        try {
            TDict result = (TDict) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TDict instance) {
        log.debug("attaching dirty TDict instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TDict instance) {
        log.debug("attaching clean TDict instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TDictDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TDictDAO) ctx.getBean("TDictDAO");
	}
}