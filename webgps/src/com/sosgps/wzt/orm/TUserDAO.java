package com.sosgps.wzt.orm;

import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TUser.
 * @see com.sosgps.wzt.orm.TUser
 * @author MyEclipse - Hibernate Tools
 */
public class TUserDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TUserDAO.class);

	//property constants
	public static final String USER_ACCOUNT = "userAccount";
	public static final String USER_PASSWORD = "userPassword";
	public static final String CREATE_BY = "createBy";
	public static final String CREATE_DATE = "createDate";
	public static final String USAGE_FLAG = "usageFlag";
	public static final String EMP_CODE = "empCode";
	public static final String USER_CONTACT = "userContact";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TUser transientInstance) {
        log.debug("saving TUser instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    public void update(TUser transientInstance) {
        log.debug("updateing TUser instance");
        try {
            getHibernateTemplate().update(transientInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
	public void delete(TUser persistentInstance) {
        log.debug("deleting TUser instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	public List findByUserAccount(Object userAccount,String empCode){
		return this.getHibernateTemplate().find("from TUser t where t.userAccount='"+userAccount +"' and t.empCode='"+empCode+"'");
		
	}
	public TUser findById( java.lang.Long id) {
        log.debug("getting TUser instance with id: " + id);
        try {
            TUser instance = (TUser) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TUser", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TUser instance) {
        log.debug("finding TUser instance by example");
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
      log.debug("finding TUser instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TUser as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByUserAccount(Object userAccount) {
		return findByProperty(USER_ACCOUNT, userAccount);
	}
	
	public List findByUserContact(Object userContact) {
		return findByProperty(USER_CONTACT, userContact);
	}	
	
	public List findByUserPassword(Object userPassword) {
		return findByProperty(USER_PASSWORD, userPassword);
	}
	
	public List findByCreateBy(Object createBy) {
		return findByProperty(CREATE_BY, createBy);
	}
	
	public List findByCreateDate(Object createDate) {
		return findByProperty(CREATE_DATE, createDate);
	}
	
	public List findByUsageFlag(Object usageFlag) {
		return findByProperty(USAGE_FLAG, usageFlag);
	}
	
	public List findByEntId(Object empCode) {
		return findByProperty(EMP_CODE, empCode);
	}
	
    public TUser merge(TUser detachedInstance) {
        log.debug("merging TUser instance");
        try {
            TUser result = (TUser) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TUser instance) {
        log.debug("attaching dirty TUser instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TUser instance) {
        log.debug("attaching clean TUser instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TUserDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TUserDAO) ctx.getBean("TUserDAO");
	}
}