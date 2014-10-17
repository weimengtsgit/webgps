package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TInactiveAlarmLoc.
 * @see com.sosgps.wzt.orm.TInactiveAlarmLoc
 * @author MyEclipse - Hibernate Tools
 */
public class TInactiveAlarmLocDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TInactiveAlarmLocDAO.class);

	//property constants
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String JMX = "jmx";
	public static final String JMY = "jmy";
	public static final String SPEED = "speed";
	public static final String DIRECTION = "direction";
	public static final String HEIGHT = "height";
	public static final String DISTANCE = "distance";
	public static final String STATLLITE_NUM = "statlliteNum";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TInactiveAlarmLoc transientInstance) {
        log.debug("saving TInactiveAlarmLoc instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TInactiveAlarmLoc persistentInstance) {
        log.debug("deleting TInactiveAlarmLoc instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TInactiveAlarmLoc findById( java.lang.Long id) {
        log.debug("getting TInactiveAlarmLoc instance with id: " + id);
        try {
            TInactiveAlarmLoc instance = (TInactiveAlarmLoc) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TInactiveAlarmLoc", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TInactiveAlarmLoc instance) {
        log.debug("finding TInactiveAlarmLoc instance by example");
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
      log.debug("finding TInactiveAlarmLoc instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TInactiveAlarmLoc as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}
	
	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}
	
	public List findByJmx(Object jmx) {
		return findByProperty(JMX, jmx);
	}
	
	public List findByJmy(Object jmy) {
		return findByProperty(JMY, jmy);
	}
	
	public List findBySpeed(Object speed) {
		return findByProperty(SPEED, speed);
	}
	
	public List findByDirection(Object direction) {
		return findByProperty(DIRECTION, direction);
	}
	
	public List findByHeight(Object height) {
		return findByProperty(HEIGHT, height);
	}
	
	public List findByDistance(Object distance) {
		return findByProperty(DISTANCE, distance);
	}
	
	public List findByStatlliteNum(Object statlliteNum) {
		return findByProperty(STATLLITE_NUM, statlliteNum);
	}
	
    public TInactiveAlarmLoc merge(TInactiveAlarmLoc detachedInstance) {
        log.debug("merging TInactiveAlarmLoc instance");
        try {
            TInactiveAlarmLoc result = (TInactiveAlarmLoc) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TInactiveAlarmLoc instance) {
        log.debug("attaching dirty TInactiveAlarmLoc instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TInactiveAlarmLoc instance) {
        log.debug("attaching clean TInactiveAlarmLoc instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TInactiveAlarmLocDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TInactiveAlarmLocDAO) ctx.getBean("TInactiveAlarmLocDAO");
	}
}