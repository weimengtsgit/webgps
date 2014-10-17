package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class ShandongLogRecv.
 * @see com.sosgps.wzt.orm.ShandongLogRecv
 * @author MyEclipse - Hibernate Tools
 */
public class ShandongLogRecvDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ShandongLogRecvDAO.class);

	//property constants
	public static final String SRCMOBILE = "srcmobile";
	public static final String LONGID = "longid";
	public static final String CONTENT_TYPE = "contentType";
	public static final String CONTENT = "content";
	public static final String CREATE_SUB_TIME = "createSubTime";

	protected void initDao() {
		//do nothing
	}
    
    public void save(ShandongLogRecv transientInstance) {
        log.debug("saving ShandongLogRecv instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ShandongLogRecv persistentInstance) {
        log.debug("deleting ShandongLogRecv instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ShandongLogRecv findById( java.lang.String id) {
        log.debug("getting ShandongLogRecv instance with id: " + id);
        try {
            ShandongLogRecv instance = (ShandongLogRecv) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.ShandongLogRecv", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ShandongLogRecv instance) {
        log.debug("finding ShandongLogRecv instance by example");
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
      log.debug("finding ShandongLogRecv instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ShandongLogRecv as model where model." 
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
	
	public List findByCreateSubTime(Object createSubTime) {
		return findByProperty(CREATE_SUB_TIME, createSubTime);
	}
	
    public ShandongLogRecv merge(ShandongLogRecv detachedInstance) {
        log.debug("merging ShandongLogRecv instance");
        try {
            ShandongLogRecv result = (ShandongLogRecv) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ShandongLogRecv instance) {
        log.debug("attaching dirty ShandongLogRecv instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ShandongLogRecv instance) {
        log.debug("attaching clean ShandongLogRecv instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ShandongLogRecvDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (ShandongLogRecvDAO) ctx.getBean("ShandongLogRecvDAO");
	}
}