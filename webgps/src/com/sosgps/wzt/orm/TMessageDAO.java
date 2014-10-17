package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TMessage.
 * @see com.sosgps.wzt.orm.TMessage
 * @author MyEclipse - Hibernate Tools
 */
public class TMessageDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TMessageDAO.class);

	//property constants
	public static final String SENDER = "sender";
	public static final String RECEIVER = "receiver";
	public static final String CONTENT = "content";
	public static final String MSG_TYPE = "msgType";
	public static final String CREATEMAN = "createman";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TMessage transientInstance) {
        log.debug("saving TMessage instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TMessage persistentInstance) {
        log.debug("deleting TMessage instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TMessage findById( java.lang.Long id) {
        log.debug("getting TMessage instance with id: " + id);
        try {
            TMessage instance = (TMessage) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TMessage", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TMessage instance) {
        log.debug("finding TMessage instance by example");
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
      log.debug("finding TMessage instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TMessage as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findBySender(Object sender) {
		return findByProperty(SENDER, sender);
	}
	
	public List findByReceiver(Object receiver) {
		return findByProperty(RECEIVER, receiver);
	}
	
	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}
	
	public List findByMsgType(Object msgType) {
		return findByProperty(MSG_TYPE, msgType);
	}
	
	public List findByCreateman(Object createman) {
		return findByProperty(CREATEMAN, createman);
	}
	
    public TMessage merge(TMessage detachedInstance) {
        log.debug("merging TMessage instance");
        try {
            TMessage result = (TMessage) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TMessage instance) {
        log.debug("attaching dirty TMessage instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TMessage instance) {
        log.debug("attaching clean TMessage instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TMessageDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TMessageDAO) ctx.getBean("TMessageDAO");
	}
}