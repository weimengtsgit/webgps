package com.sosgps.wzt.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TVersion.
 * @see com.sosgps.wzt.orm.TVersion
 * @author MyEclipse - Hibernate Tools
 */
public class TVersionDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TVersionDAO.class);

	//property constants
	public static final String VERSION = "version";
	public static final String TYPE_CODE = "typeCode";
	public static final String TYPE_DESC = "typeDesc";
	public static final String VERSION_DESC = "versionDesc";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TVersion transientInstance) {
        log.debug("saving TVersion instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TVersion persistentInstance) {
        log.debug("deleting TVersion instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TVersion findById( java.lang.Long id) {
        log.debug("getting TVersion instance with id: " + id);
        try {
            TVersion instance = (TVersion) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TVersion", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findALL(){
    	log.debug("finding ALL TVersion instance ");
        try {
            String queryString = "from TVersion as model " ;
            						
   		 return getHibernateTemplate().find(queryString);
         } catch (RuntimeException re) {
            log.error("findALL failed", re);

            throw re;
            
         }
    }
    public List findByExample(TVersion instance) {
        log.debug("finding TVersion instance by example");
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
      log.debug("finding TVersion instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TVersion as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}
	
	public List findByTypeCode(Object typeCode) {
		return findByProperty(TYPE_CODE, typeCode);
	}
	
	public List findByTypeDesc(Object typeDesc) {
		return findByProperty(TYPE_DESC, typeDesc);
	}
	
	public List findByVersionDesc(Object versionDesc) {
		return findByProperty(VERSION_DESC, versionDesc);
	}
	
    public TVersion merge(TVersion detachedInstance) {
        log.debug("merging TVersion instance");
        try {
            TVersion result = (TVersion) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TVersion instance) {
        log.debug("attaching dirty TVersion instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TVersion instance) {
        log.debug("attaching clean TVersion instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TVersionDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TVersionDAO) ctx.getBean("TVersionDAO");
	}
}