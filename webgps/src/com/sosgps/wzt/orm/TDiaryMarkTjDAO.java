package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TDiaryMarkTjDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TDiaryMarkTjDAO.class);
	public static final String ID = "id";
	public static final String TJ_DATE = "tjDate";
	public static final String DEVICE_ID = "deviceId";
	public static final String ENT_CODE = "entCode";
	public static final String USER_ID = "userId";
	public static final String DIARY_DATE = "diaryDate";
	public static final String ARRIVAL_RATE = "arrivalRate";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TDiaryMarkTj transientInstance) {
        log.debug("saving TDiaryMarkTj instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TDiaryMarkTj persistentInstance) {
        log.debug("deleting TDiaryMarkTj instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public boolean update(TDiaryMarkTj persistentInstance) {
        log.debug("update TDiaryMarkTj instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update failed", re);
            return false;
        }
    }
	
	public TDiaryMarkTj findById( Long id) {
        log.debug("getting TDiaryMarkTj instance with id: " + id);
        try {
        	TDiaryMarkTj instance = (TDiaryMarkTj) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TDiaryMarkTj", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("rawtypes")
	public List findByExample(TDiaryMarkTj instance) {
        log.debug("finding TDiaryMarkTj instance by example");
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
      log.debug("finding TDiaryMarkTj instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TDiaryMarkTj as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    public TDiaryMarkTj merge(TDiaryMarkTj detachedInstance) {
        log.debug("merging TDiaryMarkTj instance");
        try {
        	TDiaryMarkTj result = (TDiaryMarkTj) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TDiaryMarkTj instance) {
        log.debug("attaching dirtyMarkTj TDiaryMarkTj instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TDiaryMarkTj instance) {
        log.debug("attaching clean TDiaryMarkTj instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TDiaryMarkTjDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TDiaryMarkTjDAO) ctx.getBean("TDiaryMarkTjDAO");
	}
}