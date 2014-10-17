package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class ShandongSmsWait.
 * @see com.sosgps.wzt.orm.ShandongSmsWait
 * @author MyEclipse - Hibernate Tools
 */
public class ShandongSmsWaitDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ShandongSmsWaitDAO.class);

	//property constants
	public static final String LONGID = "longid";
	public static final String DESMOBILE = "desmobile";
	public static final String CONTENT = "content";
	public static final String CONTENT_TYPE = "contentType";
	public static final String RETRY = "retry";
	public static final String GWKIND = "gwkind";
	public static final String REPORT = "report";
	public static final String HWYID = "hwyid";
	public static final String MESSAGETYPE = "messagetype";

	protected void initDao() {
		//do nothing
	}
    
    public void save(ShandongSmsWait transientInstance) {
        log.debug("saving ShandongSmsWait instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ShandongSmsWait persistentInstance) {
        log.debug("deleting ShandongSmsWait instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ShandongSmsWait findById( java.lang.String id) {
        log.debug("getting ShandongSmsWait instance with id: " + id);
        try {
            ShandongSmsWait instance = (ShandongSmsWait) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.ShandongSmsWait", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ShandongSmsWait instance) {
        log.debug("finding ShandongSmsWait instance by example");
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
      log.debug("finding ShandongSmsWait instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ShandongSmsWait as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByLongid(Object longid) {
		return findByProperty(LONGID, longid);
	}
	
	public List findByDesmobile(Object desmobile) {
		return findByProperty(DESMOBILE, desmobile);
	}
	
	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}
	
	public List findByContentType(Object contentType) {
		return findByProperty(CONTENT_TYPE, contentType);
	}
	
	public List findByRetry(Object retry) {
		return findByProperty(RETRY, retry);
	}
	
	public List findByGwkind(Object gwkind) {
		return findByProperty(GWKIND, gwkind);
	}
	
	public List findByReport(Object report) {
		return findByProperty(REPORT, report);
	}
	
	public List findByHwyid(Object hwyid) {
		return findByProperty(HWYID, hwyid);
	}
	
	public List findByMessagetype(Object messagetype) {
		return findByProperty(MESSAGETYPE, messagetype);
	}
	
    public ShandongSmsWait merge(ShandongSmsWait detachedInstance) {
        log.debug("merging ShandongSmsWait instance");
        try {
            ShandongSmsWait result = (ShandongSmsWait) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ShandongSmsWait instance) {
        log.debug("attaching dirty ShandongSmsWait instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ShandongSmsWait instance) {
        log.debug("attaching clean ShandongSmsWait instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ShandongSmsWaitDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (ShandongSmsWaitDAO) ctx.getBean("ShandongSmsWaitDAO");
	}
}