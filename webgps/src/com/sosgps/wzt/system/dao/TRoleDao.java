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
	// sos���ݽ�ɫ�����ý�ɫ�Լ�����Ȩ��
	public TRole findRoleAndModulesByRoleCode(String entCode, String roleCode);
	 public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue);
	 // sos���ݽ�ɫid���ɫ�Լ�����ӵ�е�Ȩ���б�
	public TRole queryRoleAndModulesById(Long roleId);
}
