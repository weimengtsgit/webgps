package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class RefUserTgroup.
 * @see com.sosgps.wzt.orm.RefUserTgroup
 * @author MyEclipse - Hibernate Tools
 */
public class RefUserTgroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(RefUserTgroupDAO.class);

	//property constants

	protected void initDao() {
		//do nothing
	}
    
    public void save(RefUserTgroup transientInstance) {
        log.debug("saving RefUserTgroup instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(RefUserTgroup persistentInstance) {
        log.debug("deleting RefUserTgroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public RefUserTgroup findById( com.sosgps.wzt.orm.RefUserTgroupId id) {
        log.debug("getting RefUserTgroup instance with id: " + id);
        try {
            RefUserTgroup instance = (RefUserTgroup) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.RefUserTgroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(RefUserTgroup instance) {
        log.debug("finding RefUserTgroup instance by example");
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
      log.debug("finding RefUserTgroup instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from RefUserTgroup as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

    public RefUserTgroup merge(RefUserTgroup detachedInstance) {
        log.debug("merging RefUserTgroup instance");
        try {
            RefUserTgroup result = (RefUserTgroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(RefUserTgroup instance) {
        log.debug("attaching dirty RefUserTgroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(RefUserTgroup instance) {
        log.debug("attaching clean RefUserTgroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static RefUserTgroupDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (RefUserTgroupDAO) ctx.getBean("RefUserTgroupDAO");
	}
}