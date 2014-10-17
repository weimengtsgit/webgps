package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TLoginLog.
 * @see com.sosgps.wzt.orm.TLoginLog
 * @author MyEclipse - Hibernate Tools
 */
public class TLoginLogDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TLoginLogDAO.class);

	//property constants
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String EMP_CODE= "empCode";
	public static final String ACCESS_IP = "accessIp";
	public static final String LOGIN_DESC = "loginDesc";
	public static final String RESULT = "result";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TLoginLog transientInstance) {
        log.debug("saving TLoginLog instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TLoginLog persistentInstance) {
        log.debug("deleting TLoginLog instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TLoginLog findById( java.lang.Long id) {
        log.debug("getting TLoginLog instance with id: " + id);
        try {
            TLoginLog instance = (TLoginLog) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TLoginLog", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TLoginLog instance) {
        log.debug("finding TLoginLog instance by example");
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
      log.debug("finding TLoginLog instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TLoginLog as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}
	
	public List findByUserName(Object userName) {
		return findByProperty(USER_NAME, userName);
	}
	
	public List findByEntId(Object empCode) {
		return findByProperty(EMP_CODE, empCode);
	}
	
	public List findByAccessIp(Object accessIp) {
		return findByProperty(ACCESS_IP, accessIp);
	}
	
	public List findByLoginDesc(Object loginDesc) {
		return findByProperty(LOGIN_DESC, loginDesc);
	}
	
	public List findByResult(Object result) {
		return findByProperty(RESULT, result);
	}
	
    public TLoginLog merge(TLoginLog detachedInstance) {
        log.debug("merging TLoginLog instance");
        try {
            TLoginLog result = (TLoginLog) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TLoginLog instance) {
        log.debug("attaching dirty TLoginLog instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TLoginLog instance) {
        log.debug("attaching clean TLoginLog instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TLoginLogDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TLoginLogDAO) ctx.getBean("TLoginLogDAO");
	}
}