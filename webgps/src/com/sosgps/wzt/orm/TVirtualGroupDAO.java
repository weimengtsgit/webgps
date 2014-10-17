package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TVirtualGroup.
 * @see com.sosgps.wzt.orm.TVirtualGroup
 * @author MyEclipse - Hibernate Tools
 */
public class TVirtualGroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TVirtualGroupDAO.class);

	//property constants
	public static final String GROUP_NAME = "groupName";
	public static final String ENT_CODE = "entCode";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TVirtualGroup transientInstance) {
        log.debug("saving TVirtualGroup instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TVirtualGroup persistentInstance) {
        log.debug("deleting TVirtualGroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TVirtualGroup findById( java.lang.Long id) {
        log.debug("getting TVirtualGroup instance with id: " + id);
        try {
            TVirtualGroup instance = (TVirtualGroup) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TVirtualGroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TVirtualGroup instance) {
        log.debug("finding TVirtualGroup instance by example");
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
      log.debug("finding TVirtualGroup instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TVirtualGroup as model where model." 
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
	
	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}
	
    public TVirtualGroup merge(TVirtualGroup detachedInstance) {
        log.debug("merging TVirtualGroup instance");
        try {
            TVirtualGroup result = (TVirtualGroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TVirtualGroup instance) {
        log.debug("attaching dirty TVirtualGroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TVirtualGroup instance) {
        log.debug("attaching clean TVirtualGroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TVirtualGroupDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TVirtualGroupDAO) ctx.getBean("TVirtualGroupDAO");
	}
}