package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class ShandongLogSend.
 * @see com.sosgps.wzt.orm.ShandongLogSend
 * @author MyEclipse - Hibernate Tools
 */
public class ShandongLogSendDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ShandongLogSendDAO.class);

	//property constants
	public static final String LONGID = "longid";
	public static final String DESMOBILE = "desmobile";
	public static final String CONTENT = "content";
	public static final String ECODE = "ecode";
	public static final String RCODE = "rcode";
	public static final String SENDID = "sendid";
	public static final String CREATE_SUB_TIME = "createSubTime";
	public static final String CONTENT_TYPE = "contentType";
	public static final String GWKIND = "gwkind";
	public static final String CITY = "city";
	public static final String PROVINCE = "province";
	public static final String HWYID = "hwyid";
	public static final String MESSAGETYPE = "messagetype";

	protected void initDao() {
		//do nothing
	}
    
    public void save(ShandongLogSend transientInstance) {
        log.debug("saving ShandongLogSend instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(ShandongLogSend persistentInstance) {
        log.debug("deleting ShandongLogSend instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ShandongLogSend findById( java.lang.String id) {
        log.debug("getting ShandongLogSend instance with id: " + id);
        try {
            ShandongLogSend instance = (ShandongLogSend) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.ShandongLogSend", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ShandongLogSend instance) {
        log.debug("finding ShandongLogSend instance by example");
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
      log.debug("finding ShandongLogSend instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ShandongLogSend as model where model." 
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
	
	public List findByEcode(Object ecode) {
		return findByProperty(ECODE, ecode);
	}
	
	public List findByRcode(Object rcode) {
		return findByProperty(RCODE, rcode);
	}
	
	public List findBySendid(Object sendid) {
		return findByProperty(SENDID, sendid);
	}
	
	public List findByCreateSubTime(Object createSubTime) {
		return findByProperty(CREATE_SUB_TIME, createSubTime);
	}
	
	public List findByContentType(Object contentType) {
		return findByProperty(CONTENT_TYPE, contentType);
	}
	
	public List findByGwkind(Object gwkind) {
		return findByProperty(GWKIND, gwkind);
	}
	
	public List findByCity(Object city) {
		return findByProperty(CITY, city);
	}
	
	public List findByProvince(Object province) {
		return findByProperty(PROVINCE, province);
	}
	
	public List findByHwyid(Object hwyid) {
		return findByProperty(HWYID, hwyid);
	}
	
	public List findByMessagetype(Object messagetype) {
		return findByProperty(MESSAGETYPE, messagetype);
	}
	
    public ShandongLogSend merge(ShandongLogSend detachedInstance) {
        log.debug("merging ShandongLogSend instance");
        try {
            ShandongLogSend result = (ShandongLogSend) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ShandongLogSend instance) {
        log.debug("attaching dirty ShandongLogSend instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ShandongLogSend instance) {
        log.debug("attaching clean ShandongLogSend instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ShandongLogSendDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (ShandongLogSendDAO) ctx.getBean("ShandongLogSendDAO");
	}
}