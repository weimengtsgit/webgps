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
 * Data access object (DAO) for domain model class TEntTermtype.
 * @see com.sosgps.wzt.orm.TEntTermtype
 * @author MyEclipse - Hibernate Tools
 */
public class TEntTermtypeDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TEntTermtypeDAO.class);

	//property constants
	public static final String TYPE_NAME = "typeName";
	public static final String CREATEMAN = "createman";

	protected void initDao() {
		//do nothing
	}
    
	public List findAll(){
		 
	      try {
	         String queryString = "from TEntTermtype as model";
			 return getHibernateTemplate().find(queryString);
	      } catch (RuntimeException re) {
	         log.error("find by property name failed", re);
	         throw re;
	      }
		
	}
	
    public void save(TEntTermtype transientInstance) {
        log.debug("saving TEntTermtype instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TEntTermtype persistentInstance) {
        log.debug("deleting TEntTermtype instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TEntTermtype findById( java.lang.String id) {
        log.debug("getting TEntTermtype instance with id: " + id);
        try {
            TEntTermtype instance = (TEntTermtype) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TEntTermtype", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TEntTermtype instance) {
        log.debug("finding TEntTermtype instance by example");
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
      log.debug("finding TEntTermtype instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TEntTermtype as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByTypeName(Object typeName) {
		return findByProperty(TYPE_NAME, typeName);
	}
	
	public List findByCreateman(Object createman) {
		return findByProperty(CREATEMAN, createman);
	}
	
    public TEntTermtype merge(TEntTermtype detachedInstance) {
        log.debug("merging TEntTermtype instance");
        try {
            TEntTermtype result = (TEntTermtype) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TEntTermtype instance) {
        log.debug("attaching dirty TEntTermtype instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TEntTermtype instance) {
        log.debug("attaching clean TEntTermtype instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TEntTermtypeDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TEntTermtypeDAO) ctx.getBean("TEntTermtypeDAO");
	}
}