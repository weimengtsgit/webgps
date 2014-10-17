package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TArea.
 * @see com.sosgps.wzt.orm.TArea
 * @author MyEclipse - Hibernate Tools
 */
public class TAreaDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TAreaDAO.class);

	//property constants
	public static final String NAME = "name";
	public static final String XY = "xy";
	public static final String BEGIN_DATE = "beginDate";
	public static final String END_DATE = "endDate";
	public static final String EP_CODE = "epCode";
	public static final String DEVICE_ID = "deviceId";
	public static final String TYPE = "type";
	public static final String CREATEMAN = "createman";
	public static final String FLAG = "flag";
	public static final String AREA_TYPE = "areaType";
	public static final String LINE_STYLE = "lineStyle";
	public static final String LINE_COLOR = "lineColor";
	public static final String LINE_WIDTH = "lineWidth";
	public static final String LINE_ALPHA = "lineAlpha";
	public static final String FILL_COLOR = "fillColor";
	public static final String FILL_STYLE = "fillStyle";
	public static final String FILL_ALPHA = "fillAlpha";
	public static final String FILL_SHOWS = "fillShow";
	public static final String REFTERMAREAALARMS = "refTermAreaalarms";

	protected void initDao() {
		//do nothing
	}
    
    public void save(TArea transientInstance) {
        log.debug("saving TArea instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TArea persistentInstance) {
        log.debug("deleting TArea instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TArea findById( java.lang.Long id) {
        log.debug("getting TArea instance with id: " + id);
        try {
            TArea instance = (TArea) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TArea", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
	/**
	 * 
	 * @param entCode
	 * @param areaNames
	 * @param startTime
	 * @param endTime
	 * @param flag 0 模糊查询 1 准确查询
	 * @return
	 * @author zhangwei 2009-05-22
	 */
	public List findBy(String entCode,String areaNames,String startTime,String endTime,String flag){
		log.debug("finding TArea instance with property: epCode, value: " + entCode +", property: areaName, value: "+areaNames+", property: beginDate, value: "+startTime+", property: endDate, value: "+endTime);
		try {
			String queryString = "from TArea as model where model.epCode = '"+entCode+"'";
			
			if(!startTime.equals("")){
				queryString += " and model.beginDate = '"+startTime ; 
			}
			
			if(!endTime.equals("")){
				queryString += " and model.endDate = '" + endTime ;
			}
			
			if(!areaNames.equals("")){
				String[] areaName = areaNames.split(",");
				queryString += " and ( ";
				String tmp  = "";
				for(int x = 0 ; x<areaName.length ; x++){
					if(x == areaName.length-1)
						if(flag.equals("0")){
							tmp += " model.name like '%"+areaName[x]+"%'";
						}
						else{
							tmp += " model.name ='"+areaName[x]+"'";
						}
					else{
						if(flag.equals("0")){
							tmp += " model.name like '%"+areaName[x]+"%' or ";
						}else{
							tmp += " model.name ='"+areaName[x]+"' or ";
						}
					}
				}
				queryString += tmp + " )";
			}


			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
    public List findByExample(TArea instance) {
        log.debug("finding TArea instance by example");
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
      log.debug("finding TArea instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TArea as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}
	
	public List findByXy(Object xy) {
		return findByProperty(XY, xy);
	}
	
	public List findByBeginDate(Object beginDate) {
		return findByProperty(BEGIN_DATE, beginDate);
	}
	
	public List findByEndDate(Object endDate) {
		return findByProperty(END_DATE, endDate);
	}
	
	public List findByEpCode(Object epCode) {
		return findByProperty(EP_CODE, epCode);
	}
	
	public List findByDeviceId(Object deviceId) {
		return findByProperty(DEVICE_ID, deviceId);
	}
	
	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}
	
	public List findByCreateman(Object createman) {
		return findByProperty(CREATEMAN, createman);
	}
	
	public List findByFlag(Object flag) {
		return findByProperty(FLAG, flag);
	}
	
	public List findByAreaType(Object areaType) {
		return findByProperty(AREA_TYPE, areaType);
	}
	
	public List findByLineStyle(Object lineStyle) {
		return findByProperty(LINE_STYLE, lineStyle);
	}
	
	public List findByLineColor(Object lineColor) {
		return findByProperty(LINE_COLOR, lineColor);
	}
	
	public List findByLineWidth(Object lineWidth) {
		return findByProperty(LINE_WIDTH, lineWidth);
	}
	
	public List findByLineAlpha(Object lineAlpha) {
		return findByProperty(LINE_ALPHA, lineAlpha);
	}
	
	public List findByFillColor(Object fillColor) {
		return findByProperty(FILL_COLOR, fillColor);
	}
	
	public List findByFillStyle(Object fillStyle) {
		return findByProperty(FILL_STYLE, fillStyle);
	}
	
	public List findByFillAlpha(Object fillAlpha) {
		return findByProperty(FILL_ALPHA, fillAlpha);
	}
	
	public List findByFillShow(Object fillShow) {
		return findByProperty(FILL_SHOWS, fillShow);
	}
	
	public List findByRefTermAreaalarms(Object refTermAreaalarms) {
		return findByProperty(REFTERMAREAALARMS, refTermAreaalarms);
	}
	
    public TArea merge(TArea detachedInstance) {
        log.debug("merging TArea instance");
        try {
            TArea result = (TArea) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TArea instance) {
        log.debug("attaching dirty TArea instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TArea instance) {
        log.debug("attaching clean TArea instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TAreaDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TAreaDAO) ctx.getBean("TAreaDAO");
	}
}