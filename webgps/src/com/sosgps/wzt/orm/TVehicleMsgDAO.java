package com.sosgps.wzt.orm;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TVehicleMsgDAO extends HibernateDaoSupport
{
  private static final Log log = LogFactory.getLog(TVehicleMsgDAO.class);
  public static final String DEVICE_ID = "deviceId";
  public static final String ENT_CODE = "entCode";
  public static final String MODEL = "model";
  public static final String VEHICLE_NUM = "vehicleNum";
  public static final String SALE_DATE = "saleDate";
  public static final String SALE_METHODS = "saleMethods";
  public static final String DEALER = "dealer";
  public static final String INSTALLERS = "installers";
  public static final String CONTRACT_VALUE = "contractValue";
  public static final String LOAN_AMOUNT = "loanAmount";
  public static final String REPAYMENT_PERIOD = "repaymentPeriod";
  public static final String CLAIM_ACT = "claimAct";
  public static final String CRT_DATE = "crtDate";

  protected void initDao()
  {
  }

  public void save(TVehicleMsg transientInstance)
  {
    log.debug("saving TVehicleMsg instance");
    try {
      getHibernateTemplate().save(transientInstance);
      log.debug("save successful");
    } catch (RuntimeException re) {
      log.error("save failed", re);
      throw re;
    }
  }

  public void update(TVehicleMsg transientInstance) {
    log.debug("update TVehicleMsg instance");
    try {
      getHibernateTemplate().update(transientInstance);
      log.debug("update successful");
    } catch (RuntimeException re) {
      log.error("update failed", re);
      throw re;
    }
  }

  public void delete(TVehicleMsg persistentInstance) {
    log.debug("deleting TVehicleMsg instance");
    try {
      getHibernateTemplate().delete(persistentInstance);
      log.debug("delete successful");
    } catch (RuntimeException re) {
      log.error("delete failed", re);
      throw re;
    }
  }

  public TVehicleMsg findById(String id) {
    log.debug("getting TVehicleMsg instance with id: " + id);
    try {
      TVehicleMsg instance = (TVehicleMsg)getHibernateTemplate().get(
        "com.sosgps.wzt.orm.TVehicleMsg", id);
      return instance;
    } catch (RuntimeException re) {
      log.error("get failed", re);
      throw re;
    }
  }

  @SuppressWarnings("rawtypes")
public List findByExample(TVehicleMsg instance)
  {
    log.debug("finding TVehicleMsg instance by example");
    try {
      List results = getHibernateTemplate().findByExample(instance);
      log.debug("find by example successful, result size: " + 
        results.size());
      return results;
    } catch (RuntimeException re) {
      log.error("find by example failed", re);
      throw re;
    }
  }

  @SuppressWarnings("rawtypes")
public List findByProperty(String propertyName, Object value)
  {
    log.debug("finding TVehicleMsg instance with property: " + propertyName + 
      ", value: " + value);
    try {
      String queryString = "from TVehicleMsg as model where model." + 
        propertyName + "= ?";
      return getHibernateTemplate().find(queryString, value);
    } catch (RuntimeException re) {
      log.error("find by property name failed", re);
      throw re;
    }
  }

  @SuppressWarnings("rawtypes")
public List findAll()
  {
    log.debug("finding all TVehicleMsg instances");
    try {
      String queryString = "from TVehicleMsg";
      return getHibernateTemplate().find(queryString);
    } catch (RuntimeException re) {
      log.error("find all failed", re);
      throw re;
    }
  }

  public TVehicleMsg merge(TVehicleMsg detachedInstance) {
    log.debug("merging TVehicleMsg instance");
    try {
      TVehicleMsg result = (TVehicleMsg)getHibernateTemplate().merge(
        detachedInstance);
      log.debug("merge successful");
      return result;
    } catch (RuntimeException re) {
      log.error("merge failed", re);
      throw re;
    }
  }

  public void attachDirty(TVehicleMsg instance) {
    log.debug("attaching dirty TVehicleMsg instance");
    try {
      getHibernateTemplate().saveOrUpdate(instance);
      log.debug("attach successful");
    } catch (RuntimeException re) {
      log.error("attach failed", re);
      throw re;
    }
  }

  public void attachClean(TVehicleMsg instance) {
    log.debug("attaching clean TVehicleMsg instance");
    try {
      getHibernateTemplate().lock(instance, LockMode.NONE);
      log.debug("attach successful");
    } catch (RuntimeException re) {
      log.error("attach failed", re);
      throw re;
    }
  }

  public static TVehicleMsgDAO getFromApplicationContext(ApplicationContext ctx) {
    return (TVehicleMsgDAO)ctx.getBean("TVehicleMsgDAO");
  }
}