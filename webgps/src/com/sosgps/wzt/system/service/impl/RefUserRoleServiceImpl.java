package com.sosgps.wzt.system.service.impl;

import java.util.List;

import com.sosgps.wzt.orm.RefUserRole;
import com.sosgps.wzt.system.dao.RefUserRoleDao;
import com.sosgps.wzt.system.service.RefUserRoleService;

public class RefUserRoleServiceImpl implements RefUserRoleService{
	RefUserRoleDao refUserRoleDao;
	public List findByProperty(String propertyName, Object value){
		return this.refUserRoleDao.findByProperty( propertyName,  value);
	}
	
	
	public RefUserRoleDao getRefUserRoleDao() {
		return refUserRoleDao;
	}
	public void setRefUserRoleDao(RefUserRoleDao refUserRoleDao) {
		this.refUserRoleDao = refUserRoleDao;
	}
}
