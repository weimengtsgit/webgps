package com.sosgps.wzt.system.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.orm.TModuleGroupDAO;
import com.sosgps.wzt.system.dao.TModuleGroupDao;
import com.sosgps.wzt.system.service.ModuleGroupService;

public class ModuleGroupServiceImpl implements ModuleGroupService{
	
	private TModuleGroupDao tModuleGroupDao;
	// private Log log = LogUtil.getLoger(ModuleGroupService.class);
	/**
	 * 
	 * @param moduleGroup
	 */
	public void saveModuleGroup(TModuleGroup moduleGroup){
		this.tModuleGroupDao.save(moduleGroup);
		
	}
	/**
	 * 删除权限组
	 * @param id
	 */
	public void deleteModuleGroup(TModuleGroup tModuleGroup){
		
		try{
			this.tModuleGroupDao.delete(tModuleGroup);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,错误原因："+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,错误原因："+ex.getMessage(),ex);
		}
	}
	
	/**
	 * 删除权限组
	 * @param moduleGroups
	 */
	public void deleteModuleGroup(TModuleGroup[] moduleGroups){
		for(int x=0 ; x<moduleGroups.length; x++){
			this.deleteModuleGroup(moduleGroups[x]);
		}
	}
	
	public boolean deleteAll(Long[] longIds) throws Exception{
		  boolean ret = false;
		  try{
		  ret = this.tModuleGroupDao.deleteAll(longIds);
		  }catch(Exception ex){
			  SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,错误原因："+ex.getMessage(),ex);
			  throw ex;
		  }
		  return ret;
	  }
	public List getAll(){
		List list = null;
		try{
			list = this.tModuleGroupDao.findAll();
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,错误原因："+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,错误原因："+ex.getMessage(),ex);
		}
		return list;
	}
	/**
	 * 
	 * @param moduleGroup
	 */
	public void updateModuleGroup(TModuleGroup moduleGroup){
		try{
			this.tModuleGroupDao.update(moduleGroup);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E064: 权限组管理-更新权限组失败,错误原因："+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E064: 权限组管理-更新权限组失败,错误原因："+ex.getMessage(),ex);
		}
		
	}
	/**
	 * 
	 * @param level
	 * @return
	 */
	public List queryModuleGroupByLevel(Long level){
		List list = null;
		list = this.tModuleGroupDao.findByGroupLevel(level);
		return list;
	}
    
    public List queryChildGroup(Long parentId){
        List list = null;
//        Session session = HibernateUtil.currentSession();
//        String sql = "from DimModuleGroup where usageFlag='1' and parentId=:parentId";
//        try {
//			Query query = session.createQuery(sql);
//			query.setParameter("parentId",parentId);
//			list = query.list();
//		} catch (HibernateException e) {
//			 log.error(e.getMessage());
//        }finally {
//            HibernateUtil.closeSession();
//        }
        return list;
    }
	/**
	 * 
	 * @param id
	 * @return
	 */
public TModuleGroup retrieveModuleGroup(Long id){
		return this.tModuleGroupDao.findById(id);
}
	public TModuleGroupDao getTModuleGroupDao() {
		return tModuleGroupDao;
	}
	public void setTModuleGroupDao(TModuleGroupDao moduleGroupDao) {
		tModuleGroupDao = moduleGroupDao;
	}


}
