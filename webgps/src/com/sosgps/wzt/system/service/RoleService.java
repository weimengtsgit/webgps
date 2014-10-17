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
	public List getRoleByEmpCode(String empCode);//ȡֵָ����ҵ������ɫ
	public TRole retrieveRole(java.lang.Long id);
	public void updateRole(TRole role,List moduleList,Map order);
	public TRole findByRoleCode(String entCode, String roleCode);
	// sos���ݽ�ɫ�����ý�ɫ�Լ�����Ȩ��
	public TRole findRoleAndModulesByRoleCode(String entCode, String roleCode);
	public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue);
	// sos���ݽ�ɫid���ɫ�Լ�����ӵ�е�Ȩ���б�
	public TRole queryRoleAndModulesById(Long roleId);
}
