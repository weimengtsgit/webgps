package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;

public interface TModuleDao {
	 public boolean save(TModule transientInstance) ;
	 public void delete(TModule persistentInstance);
	 public boolean deleteAll(Long[] ids);
	 public List findByUsageFlag(Object usageFlag);
	 public TModule findById( java.lang.Long id) ;
	 public void update(TModule persistentInstance);
	 public List fingAllModuleList();
	 public List fingModuleList(Long[] ids);
	 public List findByModuleGradeList(Long grade);
	 public List findByModuleEmpCodeList(String  empCode);
	 public List findByProperty(String propertyName, Object value) ;
	 
	 public List findByModuleLevel(Long moduleLevel);
	 public int findNextSortedIndexByParentID(Long parentID);
	 public TModule findNextIndexModule(Long parentID,Long sortedIndex);
	 public TModule findLastIndexModule(Long parentID,Long sortedIndex);
	 public Page<TModule> listModule(String entCode, int page, int limitint,
			String searchValue);
	 public TRole findRoleAndModules(Long roleId);
	 // sos根据角色id查角色以及其所拥有的权限列表
	 public Page<TModule> queryRoleModulesById(Long roleId);
	// sos根据角色id和权限级别查其所拥有的权限列表
	public Page<TModule> queryRoleModulesByIdAndModuleGrade(Long roleId, long moduleGrade);
}
