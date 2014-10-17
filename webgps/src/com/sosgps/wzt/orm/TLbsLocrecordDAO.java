package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TLbsLocrecord.
 * @see com.sosgps.wzt.orm.TLbsLocrecord
 * @author MyEclipse - Hibernate Tools
 */
public class TLbsLocrecordDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TLbsLocrecordDAO.class);

	//property constants
	public static final String SIMCARD = "simcard";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String ENT_CODE = "entCode";
	public static final String ERROR_CODE = "errorCode";
	public static final String ERROR_DESC = "errorDesc";
	public static final String LOC_DESC = "locDesc";
	public static final String LNGX = "lngx";
	public static final String LATY = "laty";
	public static final String TYPE = "type";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TLbsLocrecord transientInstance) {
        log.debug("saving TLbsLocrecord instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TLbsLocrecord persistentInstance) {
        log.debug("deleting TLbsLocrecord instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TLbsLocrecord findById( java.lang.Long id) {
        log.debug("getting TLbsLocrecord instance with id: " + id);
        try {
            TLbsLocrecord instance = (TLbsLocrecord) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TLbsLocrecord", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TLbsLocrecord instance) {
        log.debug("finding TLbsLocrecord instance by example");
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
      log.debug("finding TLbsLocrecord instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TLbsLocrecord as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findBySimcard(Object simcard) {
		return findByProperty(SIMCARD, simcard);
	}
	
	public List findByX(Object x) {
		return findByProperty(X, x);
	}
	
	public List findByY(Object y) {
		return findByProperty(Y, y);
	}
	
	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}
	
	public List findByErrorCode(Object errorCode) {
		return findByProperty(ERROR_CODE, errorCode);
	}
	
	public List findByErrorDesc(Object errorDesc) {
		return findByProperty(ERROR_DESC, errorDesc);
	}
	
	public List findByLocDesc(Object locDesc) {
		return findByProperty(LOC_DESC, locDesc);
	}
	
	public List findByLngx(Object lngx) {
		return findByProperty(LNGX, lngx);
	}
	
	public List findByLaty(Object laty) {
		return findByProperty(LATY, laty);
	}
	
	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}
	
    public TLbsLocrecord merge(TLbsLocrecord detachedInstance) {
        log.debug("merging TLbsLocrecord instance");
        try {
            TLbsLocrecord result = (TLbsLocrecord) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TLbsLocrecord instance) {
        log.debug("attaching dirty TLbsLocrecord instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TLbsLocrecord instance) {
        log.debug("attaching clean TLbsLocrecord instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TLbsLocrecordDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TLbsLocrecordDAO) ctx.getBean("TLbsLocrecordDAO");
	}
}