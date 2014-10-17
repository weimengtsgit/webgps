package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TAlarmTjYear.
 * @see com.sosgps.wzt.orm.TAlarmTjYear
 * @author MyEclipse - Hibernate Tools
 */
public class TAlarmTjYearDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TAlarmTjYearDAO.class);

	//property constants
	public static final String DEVICE_ID = "deviceId";
	public static final String SPEED_ALARM = "speedAlarm";
	public static final String INACTIVE_ALARM = "inactiveAlarm";
	public static final String AREA_ALARM = "areaAlarm";
	public static final String LINE_ALARM = "lineAlarm";
	public static final String EMER_ALARM = "emerAlarm";
	public static final String CALL_RECORD = "callRecord";
	public static final String SHORTMESS_RECORD = "shortmessRecord";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TAlarmTjYear transientInstance) {
        log.debug("saving TAlarmTjYear instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TAlarmTjYear persistentInstance) {
        log.debug("deleting TAlarmTjYear instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TAlarmTjYear findById( java.lang.Long id) {
        log.debug("getting TAlarmTjYear instance with id: " + id);
        try {
            TAlarmTjYear instance = (TAlarmTjYear) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TAlarmTjYear", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TAlarmTjYear instance) {
        log.debug("finding TAlarmTjYear instance by example");
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
      log.debug("finding TAlarmTjYear instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TAlarmTjYear as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICE_ID, deviceId);
	}
	
	public List findBySpeedAlarm(Object speedAlarm) {
		return findByProperty(SPEED_ALARM, speedAlarm);
	}
	
	public List findByInactiveAlarm(Object inactiveAlarm) {
		return findByProperty(INACTIVE_ALARM, inactiveAlarm);
	}
	
	public List findByAreaAlarm(Object areaAlarm) {
		return findByProperty(AREA_ALARM, areaAlarm);
	}
	
	public List findByLineAlarm(Object lineAlarm) {
		return findByProperty(LINE_ALARM, lineAlarm);
	}
	
	public List findByEmerAlarm(Object emerAlarm) {
		return findByProperty(EMER_ALARM, emerAlarm);
	}
	
	public List findByCallRecord(Object callRecord) {
		return findByProperty(CALL_RECORD, callRecord);
	}
	
	public List findByShortmessRecord(Object shortmessRecord) {
		return findByProperty(SHORTMESS_RECORD, shortmessRecord);
	}
	
    public TAlarmTjYear merge(TAlarmTjYear detachedInstance) {
        log.debug("merging TAlarmTjYear instance");
        try {
            TAlarmTjYear result = (TAlarmTjYear) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TAlarmTjYear instance) {
        log.debug("attaching dirty TAlarmTjYear instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TAlarmTjYear instance) {
        log.debug("attaching clean TAlarmTjYear instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TAlarmTjYearDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TAlarmTjYearDAO) ctx.getBean("TAlarmTjYearDAO");
	}
}