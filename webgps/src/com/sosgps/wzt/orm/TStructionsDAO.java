package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TStructions.
 * @see com.sosgps.wzt.orm.TStructions
 * @author MyEclipse - Hibernate Tools
 */
public class TStructionsDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TStructionsDAO.class);

	//property constants
	public static final String INSTRUCTION = "instruction";
	public static final String STATE = "state";
	public static final String TYPE = "type";
	public static final String PARAM = "param";
	public static final String REPLY = "reply";
	public static final String CREATEMAN = "createman";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TStructions transientInstance) {
        log.debug("saving TStructions instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TStructions persistentInstance) {
        log.debug("deleting TStructions instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TStructions findById( java.lang.Long id) {
        log.debug("getting TStructions instance with id: " + id);
        try {
            TStructions instance = (TStructions) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TStructions", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TStructions instance) {
        log.debug("finding TStructions instance by example");
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
      log.debug("finding TStructions instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TStructions as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByInstruction(Object instruction) {
		return findByProperty(INSTRUCTION, instruction);
	}
	
	public List findByState(Object state) {
		return findByProperty(STATE, state);
	}
	
	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}
	
	public List findByParam(Object param) {
		return findByProperty(PARAM, param);
	}
	
	public List findByReply(Object reply) {
		return findByProperty(REPLY, reply);
	}
	
	public List findByCreateman(Object createman) {
		return findByProperty(CREATEMAN, createman);
	}
	
    public TStructions merge(TStructions detachedInstance) {
        log.debug("merging TStructions instance");
        try {
            TStructions result = (TStructions) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TStructions instance) {
        log.debug("attaching dirty TStructions instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TStructions instance) {
        log.debug("attaching clean TStructions instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TStructionsDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TStructionsDAO) ctx.getBean("TStructionsDAO");
	}
}