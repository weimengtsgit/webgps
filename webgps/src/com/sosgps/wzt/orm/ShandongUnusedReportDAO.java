package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class ShandongUnusedReport.
 * @see com.sosgps.wzt.orm.ShandongUnusedReport
 * @author MyEclipse - Hibernate Tools
 */
public class ShandongUnusedReportDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ShandongUnusedReportDAO.class);

	//property constants

	protected void initDao() {
		//do nothing
	}
    
    public void save(ShandongUnusedReport transientInstance) {
        log.debug("saving ShandongUnusedReport instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ShandongUnusedReport persistentInstance) {
        log.debug("deleting ShandongUnusedReport instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ShandongUnusedReport findById( com.sosgps.wzt.orm.ShandongUnusedReportId id) {
        log.debug("getting ShandongUnusedReport instance with id: " + id);
        try {
            ShandongUnusedReport instance = (ShandongUnusedReport) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.ShandongUnusedReport", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ShandongUnusedReport instance) {
        log.debug("finding ShandongUnusedReport instance by example");
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
      log.debug("finding ShandongUnusedReport instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ShandongUnusedReport as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

    public ShandongUnusedReport merge(ShandongUnusedReport detachedInstance) {
        log.debug("merging ShandongUnusedReport instance");
        try {
            ShandongUnusedReport result = (ShandongUnusedReport) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ShandongUnusedReport instance) {
        log.debug("attaching dirty ShandongUnusedReport instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ShandongUnusedReport instance) {
        log.debug("attaching clean ShandongUnusedReport instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ShandongUnusedReportDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (ShandongUnusedReportDAO) ctx.getBean("ShandongUnusedReportDAO");
	}
}