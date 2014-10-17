package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class RefTermGroup.
 * @see com.sosgps.wzt.orm.RefTermGroup
 * @author MyEclipse - Hibernate Tools
 */
public class RefTermGroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(RefTermGroupDAO.class);

	//property constants

	protected void initDao() {
		//do nothing
	}
    
    public void save(RefTermGroup transientInstance) {
        log.debug("saving RefTermGroup instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(RefTermGroup persistentInstance) {
        log.debug("deleting RefTermGroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public RefTermGroup findById( com.sosgps.wzt.orm.RefTermGroupId id) {
        log.debug("getting RefTermGroup instance with id: " + id);
        try {
            RefTermGroup instance = (RefTermGroup) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.RefTermGroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(RefTermGroup instance) {
        log.debug("finding RefTermGroup instance by example");
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
      log.debug("finding RefTermGroup instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from RefTermGroup as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    
 

    public RefTermGroup merge(RefTermGroup detachedInstance) {
        log.debug("merging RefTermGroup instance");
        try {
            RefTermGroup result = (RefTermGroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(RefTermGroup instance) {
        log.debug("attaching dirty RefTermGroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(RefTermGroup instance) {
        log.debug("attaching clean RefTermGroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static RefTermGroupDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (RefTermGroupDAO) ctx.getBean("RefTermGroupDAO");
	}
}