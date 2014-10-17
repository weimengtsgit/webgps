package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TAttendance.
 * @see com.sosgps.wzt.orm.TAttendance
 * @author MyEclipse - Hibernate Tools
 */
public class TAttendanceDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TAttendanceDAO.class);

	//property constants
	public static final String DEVICE_ID = "deviceId";
	public static final String ATTENDANCE_DATE = "attendanceDate";
	public static final String SIGNIN_TIME = "signinTime";
	public static final String SIGNIN_LONGITUDE = "signinLongitude";
	public static final String SIGNIN_LATITUDE = "signinLatitude";
	public static final String SIGNIN_DESC = "signinDesc";
	public static final String SIGNOFF_TIME = "signoffTime";
	public static final String SIGNOFF_LONGITUDE = "signoffLongitude";
	public static final String SIGNOFF_LATITUDE = "signoffLatitude";
	public static final String SIGNOFF_DESC = "signoffDesc";
	public static final String DELETE_FLAG = "deleteFlag";
	public static final String CREATE_TIME = "createTime";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TAttendance transientInstance) {
        log.debug("saving TAttendance instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TAttendance persisTAttendanceInstance) {
        log.debug("deleting TAttendance instance");
        try {
            getHibernateTemplate().delete(persisTAttendanceInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public void update(TAttendance persisTAttendanceInstance) {
        log.debug("update TAttendance instance");
        try {
            getHibernateTemplate().update(persisTAttendanceInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
	
	public TAttendance findById( java.lang.String id) {
        log.debug("getting TAttendance instance with id: " + id);
        try {
            TAttendance instance = (TAttendance) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TAttendance", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    @SuppressWarnings("rawtypes")
	public List findByExample(TAttendance instance) {
        log.debug("finding TAttendance instance by example");
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
      log.debug("finding TAttendance instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TAttendance as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

    public TAttendance merge(TAttendance detachedInstance) {
        log.debug("merging TAttendance instance");
        try {
            TAttendance result = (TAttendance) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TAttendance instance) {
        log.debug("attaching dirty TAttendance instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TAttendance instance) {
        log.debug("attaching clean TAttendance instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TAttendanceDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TAttendanceDAO) ctx.getBean("TAttendanceDAO");
	}
}