package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TOptLog.
 * @see com.sosgps.wzt.orm.TOptLog
 * @author MyEclipse - Hibernate Tools
 */
public class TOptLogDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TOptLogDAO.class);

	//property constants
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String EMP_CODE = "empCode";
	public static final String ACCESS_IP = "accessIp";
	public static final String OPT_DESC = "optDesc";
	public static final String FUN_FTYPE = "funFType";
	public static final String FUN_CTYPE = "funCType";
	public static final String RESULT = "result";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TOptLog transientInstance) {
        log.debug("saving TOptLog instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TOptLog persistentInstance) {
        log.debug("deleting TOptLog instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TOptLog findById( java.lang.Long id) {
        log.debug("getting TOptLog instance with id: " + id);
        try {
            TOptLog instance = (TOptLog) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TOptLog", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TOptLog instance) {
        log.debug("finding TOptLog instance by example");
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
      log.debug("finding TOptLog instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TOptLog as model where model." 
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
	
	public List findByOptDesc(Object optDesc) {
		return findByProperty(OPT_DESC, optDesc);
	}
	
	public List findByFunFType(Object funFType) {
		return findByProperty(FUN_FTYPE, funFType);
	}
	
	public List findByFunCType(Object funCType) {
		return findByProperty(FUN_CTYPE, funCType);
	}
	
	public List findByResult(Object result) {
		return findByProperty(RESULT, result);
	}
	
    public TOptLog merge(TOptLog detachedInstance) {
        log.debug("merging TOptLog instance");
        try {
            TOptLog result = (TOptLog) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TOptLog instance) {
        log.debug("attaching dirty TOptLog instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TOptLog instance) {
        log.debug("attaching clean TOptLog instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TOptLogDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TOptLogDAO) ctx.getBean("TOptLogDAO");
	}
}