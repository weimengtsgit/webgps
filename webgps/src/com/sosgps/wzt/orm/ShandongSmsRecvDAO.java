package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class ShandongSmsRecv.
 * @see com.sosgps.wzt.orm.ShandongSmsRecv
 * @author MyEclipse - Hibernate Tools
 */
public class ShandongSmsRecvDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ShandongSmsRecvDAO.class);

	//property constants
	public static final String SRCMOBILE = "srcmobile";
	public static final String LONGID = "longid";
	public static final String CONTENT_TYPE = "contentType";
	public static final String CONTENT = "content";

	protected void initDao() {
		//do nothing
	}
    
    public void save(ShandongSmsRecv transientInstance) {
        log.debug("saving ShandongSmsRecv instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ShandongSmsRecv persistentInstance) {
        log.debug("deleting ShandongSmsRecv instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ShandongSmsRecv findById( java.lang.String id) {
        log.debug("getting ShandongSmsRecv instance with id: " + id);
        try {
            ShandongSmsRecv instance = (ShandongSmsRecv) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.ShandongSmsRecv", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ShandongSmsRecv instance) {
        log.debug("finding ShandongSmsRecv instance by example");
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
      log.debug("finding ShandongSmsRecv instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ShandongSmsRecv as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findBySrcmobile(Object srcmobile) {
		return findByProperty(SRCMOBILE, srcmobile);
	}
	
	public List findByLongid(Object longid) {
		return findByProperty(LONGID, longid);
	}
	
	public List findByContentType(Object contentType) {
		return findByProperty(CONTENT_TYPE, contentType);
	}
	
	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}
	
    public ShandongSmsRecv merge(ShandongSmsRecv detachedInstance) {
        log.debug("merging ShandongSmsRecv instance");
        try {
            ShandongSmsRecv result = (ShandongSmsRecv) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ShandongSmsRecv instance) {
        log.debug("attaching dirty ShandongSmsRecv instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ShandongSmsRecv instance) {
        log.debug("attaching clean ShandongSmsRecv instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ShandongSmsRecvDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (ShandongSmsRecvDAO) ctx.getBean("ShandongSmsRecvDAO");
	}
}