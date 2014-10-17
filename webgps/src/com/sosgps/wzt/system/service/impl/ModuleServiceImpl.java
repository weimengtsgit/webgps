package com.sosgps.wzt.system.service.impl;

import java.util.List;

import com.sosgps.wzt.commons.util.DateUtil;
import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;

import com.sosgps.wzt.system.dao.TModuleDao;
import com.sosgps.wzt.system.service.ModuleService;
/**
 * 权限功能设置
 * @author Administrator
 *
 */
public class ModuleServiceImpl implements ModuleService{
	private TModuleDao tModuleDao;
	
	/**
	 * 保存权限
	 */
	public boolean saveModule(TModule module){
		return this.tModuleDao.save(module);
	}
	/**
	 * 删除权限
	 */
	public void deleteModule(TModule module){
		this.tModuleDao.delete(module);
	}
	/**
	 * 依据id查询模块
	 */
	public TModule retrieveModule(Long id){
		return this.tModuleDao.findById(id);
	}
	/*
	 * 查找所有权限 
	 */
	public List queryModuleList(Object ob){
		return this.tModuleDao.findByUsageFlag(ob);
	}
	public boolean deleteAll(Long[] longIds){
		return this.tModuleDao.deleteAll(longIds);
	}

	/**
	 * 更新权限
	 * @return
	 */
	public void updateModule(TModule module){
		try{
			this.tModuleDao.update(module);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 权限管理-删除权限失败,错误原因："+re.getMessage(),re);
		}catch(Exception ex){
			SysLogger.sysLogger.error("E062: 权限管理-删除权限失败,错误原因："+ex.getMessage(),ex);
		}
	}
	
	public List queryAllModuleList(){
		List list = null;
		try{
			list = this.tModuleDao.fingAllModuleList();
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 权限管理-查询权限失败,错误原因："+re.getMessage(),re);
		}
		return list;
	}
	public List queryModuleByGradeList(Long grade){
		List list  = null;
		try{
			list = this.tModuleDao.findByModuleGradeList(grade);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 权限管理-查询权限失败,错误原因："+re.getMessage(),re);
		}
		return list;
	}
	
	public List queryModuleByEmpCode(String empCode){
		List list  = null;
		try{
			list = this.tModuleDao.findByModuleEmpCodeList(empCode);
		}catch(RuntimeException re){
			SysLogger.sysLogger.error("E062: 权限管理-查询权限失败,错误原因："+re.getMessage(),re);
		}
		return list;
	}
	
	
	public TModuleDao getTModuleDao() {
		return tModuleDao;
	}
	public void setTModuleDao(TModuleDao moduleDao) {
		tModuleDao = moduleDao;
	}
	public TModule findByModuleCode(String moduleCode) {
		List re = tModuleDao.findByProperty("moduleCode", moduleCode);
		if(re!=null && re.size()==1){
			return (TModule)re.get(0);
		}
		return null;
	}
	public Page<TModule> listModule(String entCode, int page, int limitint,
			String searchValue) {
		try {
			return tModuleDao.listModule(entCode, page, limitint,
					searchValue);
		} catch (Exception e) {
			SysLogger.sysLogger.error("E062: 权限管理-查询权限列表失败",e);
		}
		return null;
	}
}
