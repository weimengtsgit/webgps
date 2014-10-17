package com.sosgps.wzt.orm;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class TTask.
 * @see com.sosgps.wzt.orm.TTask
 * @author MyEclipse - Hibernate Tools
 */
public class TTaskDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TTaskDAO.class);

	//property constants
	public static final String ENT_CODE = "entCode";
	public static final String TASK_NAME = "taskName";
	public static final String TASK_TYPE = "taskType";
	public static final String TASK_DESC = "taskDesc";
	public static final String TASK_POINTS = "taskPoints";
	public static final String CREATE_PERSON = "createPerson";

	protected void initDao() {
		//do nothing
	}
    

    public Long save(TTask transientInstance) {
        log.debug("saving TTask instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        return transientInstance.getTaskId();
    }  
	public void delete(TTask persistentInstance) {
        log.debug("deleting TTask instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TTask findById( java.lang.Long id) {
        log.debug("getting TTask instance with id: " + id);
        try {
            TTask instance = (TTask) getHibernateTemplate()
                    .get("com.sosgps.wzt.orm.TTask", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
	 public List findBy(String entCode,String taskIds,String taskNames,String taskTypes){
		 try {
		         String queryString = "from TTask as model where model.entCode = '"+entCode +"'"; 
		         String tmp = "";
		         if(!taskIds.equals("")){
		        	 queryString  += " and (";
		        	 String[] tasks = taskIds.split(",");
		        	 for(int x=0 ; x< tasks.length ; x++){
		        		 if(x == tasks.length-1){
		        			 tmp += " model.taskId like '%"+tasks[x]+"%' ";
		        		 }
		        		 else{
		        			 tmp += " model.taskId like '%"+tasks[x]+"%' or ";
		        		 }
		        	 }
		        	 queryString  += tmp + " ) ";
		         }
		         
		         if(!taskNames.equals("")){
		        	 queryString  += " and (";
		        	 String[] taskName = taskNames.split(",");
		        	 tmp = "";
		        	 for(int x=0 ; x< taskName.length ; x++){
		        		 if(x == taskName.length-1){
		        			 tmp += " model.taskName like '%"+taskName[x]+"%' ";
		        		 }
		        		 else{
		        			 tmp += " model.taskName like '%"+taskName[x]+"%' or ";
		        		 }
		        	 }
		        	 queryString  += tmp  + " ) ";
		         }
		         
		         if(!taskTypes.equals("")){
		        	 queryString  += " and (";
		        	 String[] taskType = taskTypes.split(",");
		        	 tmp = "";
		        	 for(int x=0 ; x< taskType.length ; x++){
		        		 if(x == taskType.length-1){
		        			 tmp += " model.taskType like '%"+taskType[x]+"%' ";
		        		 }
		        		 else{
		        			 tmp += " model.taskType like '%"+taskType[x]+"%' or ";
		        		 }
		        	 }
		        	 queryString  += tmp + " ) ";
		         }
				 return getHibernateTemplate().find(queryString);
		      } catch (RuntimeException re) {
		         log.error("find by property name failed", re);
		         throw re;
		      }
	
	 }
	 public List findByTaskName(Object entCode,Object taskName){
		 log.debug("finding TTask instance with property: " + entCode
		            + ", value: " + entCode + ", property: " + taskName + ", value: "+taskName);
		      try {
		         String queryString = "from TTask as model where model.entCode = '"+entCode+"' and model.taskName like '%"+taskName +"%'" ; 
		         		
				 return getHibernateTemplate().find(queryString);
		      } catch (RuntimeException re) {
		         log.error("find by property name failed", re);
		         throw re;
		      }
		 

	 }
	 public void deleteIn(String entCode,String ids){
		  try {
		         String queryString = "from TTask as model where model.entCode = '"+entCode+"' and model.taskId in ("+ids+")" ; 
		         		
				 List list =  getHibernateTemplate().find(queryString);
				 if(list.size()!= 0){
					 getHibernateTemplate().deleteAll(list); 
				 }
		      } catch (RuntimeException re) {
		         log.error("deleteIn by property entCode,ids failed", re);
		         throw re;
		      }
	 }
	 public void update(TTask task){
		 try {

			 getHibernateTemplate().update(task);
		
	      } catch (RuntimeException re) {
	         log.error("update Task failed", re);
	         throw re;
	      }
	 }
    public List findByExample(TTask instance) {
        log.debug("finding TTask instance by example");
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
      log.debug("finding TTask instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TTask as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByEntCode(Object entCode) {
		return findByProperty(ENT_CODE, entCode);
	}
	
	public List findByTaskName(Object taskName) {
		return findByProperty(TASK_NAME, taskName);
	}
	
	public List findByTaskType(Object taskType) {
		return findByProperty(TASK_TYPE, taskType);
	}
	
	public List findByTaskDesc(Object taskDesc) {
		return findByProperty(TASK_DESC, taskDesc);
	}
	
	public List findByTaskPoints(Object taskPoints) {
		return findByProperty(TASK_POINTS, taskPoints);
	}
	
	public List findByCreatePerson(Object createPerson) {
		return findByProperty(CREATE_PERSON, createPerson);
	}
	
    public TTask merge(TTask detachedInstance) {
        log.debug("merging TTask instance");
        try {
            TTask result = (TTask) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TTask instance) {
        log.debug("attaching dirty TTask instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TTask instance) {
        log.debug("attaching clean TTask instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TTaskDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TTaskDAO) ctx.getBean("TTaskDAO");
	}
}