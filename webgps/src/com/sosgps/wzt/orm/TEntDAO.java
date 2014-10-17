package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TEnt.
 * @see com.sosgps.wzt.orm.TEnt
 * @author MyEclipse - Hibernate Tools
 */
public class TEntDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TEntDAO.class);

	//property constants
	public static final String ENT_CODE = "entCode";
	public static final String ENT_NAME = "entName";
	public static final String ENT_STATUS = "entStatus";
	public static final String CENTER_X = "centerX";
	public static final String CENTER_Y = "centerY";
	public static final String MAP_ZOOM = "mapZoom";
	public static final String LOGO_URL = "logoUrl";
	public static final String FEE_TYPE = "feeType";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TEnt transientInstance) {
        log.debug("saving TEnt instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TEnt persistentInstance) {
        log.debug("deleting TEnt instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public void update(TEnt persistentInstance) {
        log.debug("update TEnt instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
	
	public TEnt findById( java.lang.String id) {
        log.debug("getting TEnt instance with id: " + id);
        try {
            TEnt instance = (TEnt) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TEnt", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TEnt instance) {
        log.debug("finding TEnt instance by example");
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
      log.debug("finding TEnt instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TEnt as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByEntName(Object entName) {
		return findByProperty(ENT_NAME, entName);
	}
	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}
	
	public List findByEntStatus(Object entStatus) {
		return findByProperty(ENT_STATUS, entStatus);
	}
	
	public List findByCenterX(Object centerX) {
		return findByProperty(CENTER_X, centerX);
	}
	
	public List findByCenterY(Object centerY) {
		return findByProperty(CENTER_Y, centerY);
	}
	
	public List findByMapZoom(Object mapZoom) {
		return findByProperty(MAP_ZOOM, mapZoom);
	}
	
	public List findByLogoUrl(Object logoUrl) {
		return findByProperty(LOGO_URL, logoUrl);
	}
	
	public List findByFeeType(Object feeType) {
		return findByProperty(FEE_TYPE, feeType);
	}
	
    public TEnt merge(TEnt detachedInstance) {
        log.debug("merging TEnt instance");
        try {
            TEnt result = (TEnt) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TEnt instance) {
        log.debug("attaching dirty TEnt instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TEnt instance) {
        log.debug("attaching clean TEnt instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TEntDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TEntDAO) ctx.getBean("TEntDAO");
	}
}