package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class RefTermVirgroup.
 * @see com.sosgps.wzt.orm.RefTermVirgroup
 * @author MyEclipse - Hibernate Tools
 */
public class RefTermVirgroupDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(RefTermVirgroupDAO.class);

	//property constants

	protected void initDao() {
		//do nothing
	}
    
    public void save(RefTermVirgroup transientInstance) {
        log.debug("saving RefTermVirgroup instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(RefTermVirgroup persistentInstance) {
        log.debug("deleting RefTermVirgroup instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public RefTermVirgroup findById( java.lang.Long id) {
        log.debug("getting RefTermVirgroup instance with id: " + id);
        try {
            RefTermVirgroup instance = (RefTermVirgroup) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.RefTermVirgroup", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(RefTermVirgroup instance) {
        log.debug("finding RefTermVirgroup instance by example");
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
      log.debug("finding RefTermVirgroup instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from RefTermVirgroup as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

    public RefTermVirgroup merge(RefTermVirgroup detachedInstance) {
        log.debug("merging RefTermVirgroup instance");
        try {
            RefTermVirgroup result = (RefTermVirgroup) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(RefTermVirgroup instance) {
        log.debug("attaching dirty RefTermVirgroup instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(RefTermVirgroup instance) {
        log.debug("attaching clean RefTermVirgroup instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static RefTermVirgroupDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (RefTermVirgroupDAO) ctx.getBean("RefTermVirgroupDAO");
	}
}