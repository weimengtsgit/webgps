package com.sosgps.wzt.system.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.dao.TRoleDao;
import com.sosgps.wzt.system.service.RoleService;
/**
 * 角色管理服务
 * @author zhangwei
 *
 */
public class RoleServiceImpl implements RoleService{
	private TRoleDao tRoleDao;
	
	public void saveRole(TRole role){
		try{
			this.tRoleDao.save(role);

		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 角色管理-保存角色失败,错误原因："+re.getMessage(),re);
			throw re;
		}
	}
	public void deleteRole(TRole role){
		
	}
	
	public TRole retrieveRole(java.lang.Long id){
		TRole tRole = null;
		try{
			tRole = this.tRoleDao.findById(id);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 角色管理-获取角色失败,错误原因："+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: 角色管理-获取角色失败,错误原因："+ex.getMessage(),ex);
		}
		return tRole;
	}
	
	public List getAllRole(){
		return this.tRoleDao.findAll();
	}
	public List getRoleByEmpCode(String empCode){
		return this.tRoleDao.findByEmpCode(empCode);
	}
	public void updateRole(TRole role){
		try{
			 this.tRoleDao.update(role);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 角色管理-更新角色失败,错误原因："+re.getMessage(),re);
			re.fillInStackTrace();
			throw re;
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: 角色管理-更新角色失败,错误原因："+ex.getMessage(),ex);
			ex.fillInStackTrace();
		}
	}
	
	public void updateRole(TRole role,List moduleList,Map order){
		this.tRoleDao.update(role);
		/*更新权限角色关系表
		  首先删除对应角色的权限
		  重新添加角色权限关系
		*/
	}
	/**
	 * 删除角色
	 * 删除角色需要删除角色所关联的用户
	 */
	public boolean deleteAll(Long[] longIds){
		return this.tRoleDao.deleteAll(longIds);
		
	}
	public TRoleDao getTRoleDao() {
		return tRoleDao;
	}
	public void setTRoleDao(TRoleDao roleDao) {
		tRoleDao = roleDao;
	}
	
	
	public TRole findByRoleCode(String entCode, String roleCode) {
		TRole instance = new TRole();
		instance.setEmpCode(entCode);
		instance.setRoleCode(roleCode);
		List re = tRoleDao.findByExample(instance);
		if(re!=null && re.size()==1)
			return (TRole)re.get(0);
		return null;
	}
	
	public TRole findRoleAndModulesByRoleCode(String entCode, String roleCode) {
		try {
			return tRoleDao.findRoleAndModulesByRoleCode(entCode, roleCode);
		} catch (Exception e) {
			SysLogger.sysLogger.error("根据角色代码获得角色以及下属权限错误", e);
			return null;
		}
	}
	
	public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue) {
		try{
			 return this.tRoleDao.listRole(entCode, page, limitint,
						searchValue);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: 角色管理-查询角色列表失败,错误原因："+ex.getMessage(),ex);
			ex.fillInStackTrace();
		}
		return null;
	}
	public TRole queryRoleAndModulesById(Long roleId) {
		try {
			return tRoleDao.queryRoleAndModulesById(roleId);
		} catch (Exception e) {
			SysLogger.sysLogger.error("根据角色id查角色以及其所拥有的权限列表错误", e);
			return null;
		}
	}
}
