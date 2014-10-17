package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TCallRecord.
 * @see com.sosgps.wzt.orm.TCallRecord
 * @author MyEclipse - Hibernate Tools
 */
public class TCallRecordDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TCallRecordDAO.class);

	//property constants
	public static final String CALLER_NUMBER = "callerNumber";
	public static final String CALLED_NUMBER = "calledNumber";
	public static final String DEVICE_ID = "deviceId";
	public static final String CALL_TYPE = "callType";
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TCallRecord transientInstance) {
        log.debug("saving TCallRecord instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TCallRecord persistentInstance) {
        log.debug("deleting TCallRecord instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TCallRecord findById( java.lang.Long id) {
        log.debug("getting TCallRecord instance with id: " + id);
        try {
            TCallRecord instance = (TCallRecord) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TCallRecord", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TCallRecord instance) {
        log.debug("finding TCallRecord instance by example");
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
      log.debug("finding TCallRecord instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TCallRecord as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByCallerNumber(Object callerNumber) {
		return findByProperty(CALLER_NUMBER, callerNumber);
	}
	
	public List findByCalledNumber(Object calledNumber) {
		return findByProperty(CALLED_NUMBER, calledNumber);
	}
	
	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICE_ID, deviceId);
	}
	
	public List findByCallType(Object callType) {
		return findByProperty(CALL_TYPE, callType);
	}
	
	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}
	
	public List findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}
	
    public TCallRecord merge(TCallRecord detachedInstance) {
        log.debug("merging TCallRecord instance");
        try {
            TCallRecord result = (TCallRecord) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TCallRecord instance) {
        log.debug("attaching dirty TCallRecord instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TCallRecord instance) {
        log.debug("attaching clean TCallRecord instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TCallRecordDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TCallRecordDAO) ctx.getBean("TCallRecordDAO");
	}
}