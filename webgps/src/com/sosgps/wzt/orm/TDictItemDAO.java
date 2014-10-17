package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TDictItem.
 * @see com.sosgps.wzt.orm.TDictItem
 * @author MyEclipse - Hibernate Tools
 */
public class TDictItemDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TDictItemDAO.class);

	//property constants
	public static final String ITEM_NAME = "itemName";
	public static final String ITEM_CODE = "itemCode";
	public static final String DICT_CODE = "dictCode";
	public static final String USAGE_FLAG = "usageFlag";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TDictItem transientInstance) {
        log.debug("saving TDictItem instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TDictItem persistentInstance) {
        log.debug("deleting TDictItem instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TDictItem findById( java.lang.Long id) {
        log.debug("getting TDictItem instance with id: " + id);
        try {
            TDictItem instance = (TDictItem) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TDictItem", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TDictItem instance) {
        log.debug("finding TDictItem instance by example");
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
      log.debug("finding TDictItem instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TDictItem as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByItemName(Object itemName) {
		return findByProperty(ITEM_NAME, itemName);
	}
	
	public List findByItemCode(Object itemCode) {
		return findByProperty(ITEM_CODE, itemCode);
	}
	
	public List findByDictCode(Object dictCode) {
		return findByProperty(DICT_CODE, dictCode);
	}
	
	public List findByUsageFlag(Object usageFlag) {
		return findByProperty(USAGE_FLAG, usageFlag);
	}
	
    public TDictItem merge(TDictItem detachedInstance) {
        log.debug("merging TDictItem instance");
        try {
            TDictItem result = (TDictItem) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TDictItem instance) {
        log.debug("attaching dirty TDictItem instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TDictItem instance) {
        log.debug("attaching clean TDictItem instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TDictItemDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TDictItemDAO) ctx.getBean("TDictItemDAO");
	}
}