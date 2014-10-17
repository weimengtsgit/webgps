package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TDiaryDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TDiaryDAO.class);
	public static final String ID = "id";
	public static final String TERM_NAME = "termName";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String REMARK = "remark";
	public static final String CREATE_DATE = "createDate";
	public static final String MODIFY_DATE = "modifyDate";
	public static final String USER_ID = "userId";
	public static final String ENT_CODE = "entCode";
	public static final String DIARY_DATE = "diaryDate";
	public static final String REMARK_DATE = "remarkDate";
	public static final String DEVICE_ID = "deviceId";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TDiary transientInstance) {
        log.debug("saving TDiary instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TDiary persistentInstance) {
        log.debug("deleting TDiary instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public boolean update(TDiary persistentInstance) {
        log.debug("update TDiary instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update failed", re);
            return false;
        }
    }
	
	public TDiary findById( Long id) {
        log.debug("getting TDiary instance with id: " + id);
        try {
        	TDiary instance = (TDiary) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TDiary", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("rawtypes")
	public List findByExample(TDiary instance) {
        log.debug("finding TDiary instance by example");
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
      log.debug("finding TDiary instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TDiary as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    @SuppressWarnings("rawtypes")
	public List findByTermName(Object termName) {
		return findByProperty(TERM_NAME, termName);
	}
	@SuppressWarnings("rawtypes")
	public List findByTITLE(Object title) {
		return findByProperty(TITLE, title);
	}
	
    public TDiary merge(TDiary detachedInstance) {
        log.debug("merging TDiary instance");
        try {
        	TDiary result = (TDiary) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TDiary instance) {
        log.debug("attaching dirty TDiary instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TDiary instance) {
        log.debug("attaching clean TDiary instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TDiaryDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TDiaryDAO) ctx.getBean("TDiaryDAO");
	}
}