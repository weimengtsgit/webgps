package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TCitytrafficinfo.
 * @see com.sosgps.wzt.orm.TCitytrafficinfo
 * @author MyEclipse - Hibernate Tools
 */
public class TCitytrafficinfoDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TCitytrafficinfoDAO.class);

	//property constants
	public static final String SP = "sp";
	public static final String CONTENTS = "contents";
	public static final String INTERSECTION = "intersection";
	public static final String LIFTINGTIME = "liftingtime";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String DIRCTION="dirction";
	public static final String FLAG = "flag";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TCitytrafficinfo transientInstance) {
        log.debug("saving TCitytrafficinfo instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    public void saveOrUpdate(TCitytrafficinfo transientInstance) {
        log.debug("saving TCitytrafficinfo instance");
        try {
            getHibernateTemplate().saveOrUpdate(transientInstance);
            log.debug("saveOrUpdate successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
	public void delete(TCitytrafficinfo persistentInstance) {
        log.debug("deleting TCitytrafficinfo instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
           
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TCitytrafficinfo findById( java.lang.Long id) {
        log.debug("getting TCitytrafficinfo instance with id: " + id);
        try {
            TCitytrafficinfo instance = (TCitytrafficinfo) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TCitytrafficinfo", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TCitytrafficinfo instance) {
        log.debug("finding TCitytrafficinfo instance by example");
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
      log.debug("finding TCitytrafficinfo instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TCitytrafficinfo as model where model." 
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
	
	public List findByIntersection(Object intersection) {
		return findByProperty(INTERSECTION, intersection);
	}
	
	public List findByLiftingtime(Object liftingtime) {
		return findByProperty(LIFTINGTIME, liftingtime);
	}
	
	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}
	
	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}
	
	public List findByFlag(Object flag) {
		return findByProperty(FLAG, flag);
	}
	
    public TCitytrafficinfo merge(TCitytrafficinfo detachedInstance) {
        log.debug("merging TCitytrafficinfo instance");
        try {
            TCitytrafficinfo result = (TCitytrafficinfo) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TCitytrafficinfo instance) {
        log.debug("attaching dirty TCitytrafficinfo instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TCitytrafficinfo instance) {
        log.debug("attaching clean TCitytrafficinfo instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TCitytrafficinfoDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TCitytrafficinfoDAO) ctx.getBean("TCitytrafficinfoDAO");
	}
}