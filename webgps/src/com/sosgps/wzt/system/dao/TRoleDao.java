package com.sosgps.wzt.system.dao;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TRole;

public interface TRoleDao {
	 public void save(TRole transientInstance) ;
	 public void update(TRole transientInstance);
	 public void delete(TRole persistentInstance);
	 public boolean deleteAll(Long[] ids);
	 public List findAll();
	 public List findByEmpCode(Object empCode) ;
	 public TRole findById( java.lang.Long id);
	 public List findByProperty(String propertyName, Object value);
	 public List findByExample(TRole instance);
	// sos根据角色代码获得角色以及下属权限
	public TRole findRoleAndModulesByRoleCode(String entCode, String roleCode);
	 public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue);
	 // sos根据角色id查角色以及其所拥有的权限列表
	public TRole queryRoleAndModulesById(Long roleId);
}
