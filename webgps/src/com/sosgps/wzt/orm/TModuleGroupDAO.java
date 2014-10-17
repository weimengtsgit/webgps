package com.sosgps.wzt.orm;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TModuleGroup.
 * @see com.sosgps.wzt.orm.TModuleGroup
 * @author MyEclipse - Hibernate Tools
 */
public class TModuleGroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TModuleGroupDAO.class);

	//property constants
	public static final String GROUP_NAME = "groupName";
	public static final String GROUP_LEVEL = "groupLevel";
	public static final String GROUP_DESC = "groupDesc";
	public static final String PARENT_ID = "parentId";
	public static final String CREATE_BY = "createBy";
	public static final String CREATE_DATE = "createDate";
	public static final String USAGE_FLAG = "usageFlag";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TModuleGroup transientInstance) {
        log.debug("saving TModuleGroup instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
	public void update(TModuleGroup persistentInstance){
		
        log.debug("updating TModuleGroup instance");
        try {
        	getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
		
	}
    public List findAll(){ 
    	log.debug("finding all TModuleGroup instance with property" );
          try {
              String queryString = "from TModuleGroup as model where model." 
					+ USAGE_FLAG + "= ?";
             return getHibernateTemplate().find(queryString, "1");
        	 
          } catch (RuntimeException re) {
             log.error("find by all  failed", re);
             throw re;
          }
    }
	public void delete(TModuleGroup persistentInstance) {
        log.debug("deleting TModuleGroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TModuleGroup findById( java.lang.Long id) {
        log.debug("getting TModuleGroup instance with id: " + id);
        try {
            TModuleGroup instance = (TModuleGroup) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TModuleGroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TModuleGroup instance) {
        log.debug("finding TModuleGroup instance by example");
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
      log.debug("finding TModuleGroup instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TModuleGroup as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByGroupName(Object groupName) {
		return findByProperty(GROUP_NAME, groupName);
	}
	
	public List findByGroupLevel(Object groupLevel) {
		return findByProperty(GROUP_LEVEL, groupLevel);
	}
	
	public List findByGroupDesc(Object groupDesc) {
		return findByProperty(GROUP_DESC, groupDesc);
	}
	
	public List findByParentId(Object parentId) {
		return findByProperty(PARENT_ID, parentId);
	}
	
	public List findByCreateBy(Object createBy) {
		return findByProperty(CREATE_BY, createBy);
	}
	
	public List findByCreateDate(Object createDate) {
		return findByProperty(CREATE_DATE, createDate);
	}
	
	public List findByUsageFlag(Object usageFlag) {
		return findByProperty(USAGE_FLAG, usageFlag);
	}
	
    public TModuleGroup merge(TModuleGroup detachedInstance) {
        log.debug("merging TModuleGroup instance");
        try {
            TModuleGroup result = (TModuleGroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TModuleGroup instance) {
        log.debug("attaching dirty TModuleGroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TModuleGroup instance) {
        log.debug("attaching clean TModuleGroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TModuleGroupDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TModuleGroupDAO) ctx.getBean("TModuleGroupDAO");
	}
}