package com.sosgps.wzt.system.service;

import java.util.List;
import java.util.Map;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TRole;

public interface RoleService {
	public void saveRole(TRole role);
	public void updateRole(TRole role);
	public void deleteRole(TRole role);
	public boolean deleteAll(Long[] longIds);
	public List getAllRole();
	public List getRoleByEmpCode(String empCode);//取值指定企业所属角色
	public TRole retrieveRole(java.lang.Long id);
	public void updateRole(TRole role,List moduleList,Map order);
	public TRole findByRoleCode(String entCode, String roleCode);
	// sos根据角色代码获得角色以及下属权限
	public TRole findRoleAndModulesByRoleCode(String entCode, String roleCode);
	public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue);
	// sos根据角色id查角色以及其所拥有的权限列表
	public TRole queryRoleAndModulesById(Long roleId);
}
