package com.sosgps.wzt.system.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.dao.RefModuleRoleDao;
import com.sosgps.wzt.system.service.RefModuleRoleService;

public class RefModuleRoleServiceImpl implements RefModuleRoleService{
	private RefModuleRoleDao refModuleRoleDao;
	
	public void saveModuleRole(RefModuleRole transientInstance){
		this.refModuleRoleDao.save(transientInstance);
	}
	/**
	 * 添加权限与角色对应关系sos
	 */
	public void saveModuleRole(TRole role,List moduleList,Map orderMap)throws Exception {
		RefModuleRole refModuleRole = new RefModuleRole();
		try{
		for(Iterator it = moduleList.iterator();it.hasNext();){
			String moduleid = (String)it.next();
			TModule tmodule = new TModule();
			tmodule.setId(Long.parseLong(moduleid));
			
			
			//Long order = Long.valueOf((String)orderMap.get(String.valueOf(module.getId())));
			refModuleRole = new RefModuleRole();
			refModuleRole.setTRole(role);
			refModuleRole.setTModule(tmodule);
			//refModuleRole.setModuleOrder(order);
			refModuleRoleDao.save(refModuleRole);
			
		}
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E065: 角色权限管理-增加角色权限关系失败！ "+re);
			throw re;
		}catch(Exception ex){
			SysLogger.sysLogger.error("E065: 角色权限管理-增加角色权限关系失败！ "+ex);
			throw ex;
		}
	}
	/**
	 * 删除对应角色所有权限(ref_module_role)
	 * 重新加入权限
	 */
	public void updateModuleRole(TRole role,List moduleList,Map orderMap)throws Exception {
		RefModuleRole refModuleRole = new RefModuleRole();
		try{
			//首先删除角色所有权限
			this.refModuleRoleDao.delete(role.getId());
			for(Iterator it = moduleList.iterator();it.hasNext();){
				String moduleid = (String)it.next();
				TModule tmodule = new TModule();
				tmodule.setId(Long.parseLong(moduleid));
				
				
				//Long order = Long.valueOf((String)orderMap.get(String.valueOf(module.getId())));
				refModuleRole = new RefModuleRole();
				refModuleRole.setTRole(role);
				refModuleRole.setTModule(tmodule);
				//refModuleRole.setModuleOrder(order);
				refModuleRoleDao.save(refModuleRole);
				
			}

		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E065: 角色权限管理-更新角色权限关系失败！ "+re);
			throw re;
		}catch(Exception ex){
			SysLogger.sysLogger.error("E065: 角色权限管理-更新角色权限关系失败！ "+ex);
			throw ex;
		}
	}
	public RefModuleRoleDao getRefModuleRoleDao() {
		return refModuleRoleDao;
	}
	public void setRefModuleRoleDao(RefModuleRoleDao refModuleRoleDao) {
		this.refModuleRoleDao = refModuleRoleDao;
	}
}
