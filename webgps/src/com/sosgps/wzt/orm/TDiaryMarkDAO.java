package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TDiaryMarkDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TDiaryMarkDAO.class);
	public static final String ID = "id";
	public static final String DEVICE_ID = "deviceId";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String CREATE_DATE = "createDate";
	public static final String CHANGE_DATE = "changeDate";
	public static final String USER_ID = "userId";
	public static final String ENT_CODE = "entCode";
	public static final String DIARY_DATE = "diaryDate";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TDiaryMark transientInstance) {
        log.debug("saving TDiaryMark instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TDiaryMark persistentInstance) {
        log.debug("deleting TDiary instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public boolean update(TDiaryMark persistentInstance) {
        log.debug("update TDiaryMark instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update failed", re);
            return false;
        }
    }
	
	public TDiaryMark findById( Long id) {
        log.debug("getting TDiaryMark instance with id: " + id);
        try {
        	TDiaryMark instance = (TDiaryMark) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TDiaryMark", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("rawtypes")
	public List findByExample(TDiaryMark instance) {
        log.debug("finding TDiaryMark instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
    @SuppressWarnings("rawtypes")
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding TDiaryMark instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TDiaryMark as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    public TDiaryMark merge(TDiaryMark detachedInstance) {
        log.debug("merging TDiaryMark instance");
        try {
        	TDiaryMark result = (TDiaryMark) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TDiaryMark instance) {
        log.debug("attaching dirtyMark TDiaryMark instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TDiaryMark instance) {
        log.debug("attaching clean TDiaryMark instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TDiaryMarkDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TDiaryMarkDAO) ctx.getBean("TDiaryMarkDAO");
	}
}