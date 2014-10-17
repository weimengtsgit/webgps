package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class THiwaytrafficinfo.
 * @see com.sosgps.wzt.orm.THiwaytrafficinfo
 * @author MyEclipse - Hibernate Tools
 */
public class THiwaytrafficinfoDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(THiwaytrafficinfoDAO.class);

	//property constants
	public static final String SP = "sp";
	public static final String CONTENTS = "contents";
	public static final String STATUS = "status";
	public static final String HIWAYNAME = "hiwayname";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String DRICTION = "driction";
	public static final String PTYPE = "ptype";
	public static final String GATE = "gate";
	public static final String MILEAGE = "mileage";
	public static final String STARTPOINT = "startpoint";
	public static final String ENDPOINT = "endpoint";
	public static final String FLAG = "flag";
	public static final String HIGHWAYID = "hiwayid";

	protected void initDao() {
		//do nothing
	}
    
    public void save(THiwaytrafficinfo transientInstance) {
        log.debug("saving THiwaytrafficinfo instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    public void update(THiwaytrafficinfo transientInstance) {
        log.debug("saving THiwaytrafficinfo instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
	public void delete(THiwaytrafficinfo persistentInstance) {
        log.debug("deleting THiwaytrafficinfo instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public THiwaytrafficinfo findById( java.lang.Long id) {
        log.debug("getting THiwaytrafficinfo instance with id: " + id);
        try {
            THiwaytrafficinfo instance = (THiwaytrafficinfo) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.THiwaytrafficinfo", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(THiwaytrafficinfo instance) {
        log.debug("finding THiwaytrafficinfo instance by example");
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
      log.debug("finding THiwaytrafficinfo instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from THiwaytrafficinfo as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
	public List findBySp(Object sp) {
		return findByProperty(SP, sp);
	}
	
	public List findByContents(Object contents) {
		return findByProperty(CONTENTS, contents);
	}
	
	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}
	
	public List findByHiwayname(Object hiwayname) {
		return findByProperty(HIWAYNAME, hiwayname);
	}
	
	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}
	
	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}
	
	public List findByDriction(Object driction) {
		return findByProperty(DRICTION, driction);
	}
	
	public List findByPtype(Object ptype) {
		return findByProperty(PTYPE, ptype);
	}
	
	public List findByGate(Object gate) {
		return findByProperty(GATE, gate);
	}
	
	public List findByMileage(Object mileage) {
		return findByProperty(MILEAGE, mileage);
	}
	
	public List findByStartpoint(Object startpoint) {
		return findByProperty(STARTPOINT, startpoint);
	}
	
	public List findByEndpoint(Object endpoint) {
		return findByProperty(ENDPOINT, endpoint);
	}
	
	public List findByFlag(Object flag) {
		return findByProperty(FLAG, flag);
	}
	
    public THiwaytrafficinfo merge(THiwaytrafficinfo detachedInstance) {
        log.debug("merging THiwaytrafficinfo instance");
        try {
            THiwaytrafficinfo result = (THiwaytrafficinfo) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(THiwaytrafficinfo instance) {
        log.debug("attaching dirty THiwaytrafficinfo instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(THiwaytrafficinfo instance) {
        log.debug("attaching clean THiwaytrafficinfo instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static THiwaytrafficinfoDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (THiwaytrafficinfoDAO) ctx.getBean("THiwaytrafficinfoDAO");
	}
}