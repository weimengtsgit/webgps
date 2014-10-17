package com.sosgps.wzt.system.dao;

import com.sosgps.wzt.orm.RefModuleRole;

public interface RefModuleRoleDao {
	public void save(RefModuleRole transientInstance);
	public void update(RefModuleRole transientInstance);
	public void delete(RefModuleRole transientInstance);
	public boolean delete(Long id);
}
