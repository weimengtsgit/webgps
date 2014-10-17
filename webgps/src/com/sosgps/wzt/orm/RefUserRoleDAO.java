package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class RefUserRole.
 * @see com.sosgps.wzt.orm.RefUserRole
 * @author MyEclipse - Hibernate Tools
 */
public class RefUserRoleDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(RefUserRoleDAO.class);

	//property constants

	protected void initDao() {
		//do nothing
	}
    
    public void save(RefUserRole transientInstance) {
        log.debug("saving RefUserRole instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(RefUserRole persistentInstance) {
        log.debug("deleting RefUserRole instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	 public void update(RefUserRole transientInstance) {
	        log.debug("updateing RefUserRole instance");
	        try {
	            getHibernateTemplate().update(transientInstance);
	           
	            log.debug("update successful");
	        } catch (RuntimeException re) {
	            log.error("update failed", re);
	            throw re;
	        }
	    }

    public RefUserRole findById( java.lang.Long id) {
        log.debug("getting RefUserRole instance with id: " + id);
        try {
            RefUserRole instance = (RefUserRole) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.RefUserRole", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(RefUserRole instance) {
        log.debug("finding RefUserRole instance by example");
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
      log.debug("finding RefUserRole instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from RefUserRole as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

    public RefUserRole merge(RefUserRole detachedInstance) {
        log.debug("merging RefUserRole instance");
        try {
            RefUserRole result = (RefUserRole) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(RefUserRole instance) {
        log.debug("attaching dirty RefUserRole instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(RefUserRole instance) {
        log.debug("attaching clean RefUserRole instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static RefUserRoleDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (RefUserRoleDAO) ctx.getBean("RefUserRoleDAO");
	}
}